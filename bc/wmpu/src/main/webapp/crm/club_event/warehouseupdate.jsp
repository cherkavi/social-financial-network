<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcCRMBean"  />

<%= Bean.getLogOutScript(request) %>

<%@page import="java.util.HashMap"%>
<%@page import="bc.objects.bcLGObject"%>
<%@page import="java.util.Enumeration"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Set"%>
<%@page import="java.util.Iterator"%>
<%@page import="bc.objects.bcClubShortObject"%>
<%@page import="bc.objects.bcLGGiftObject"%><html>
<head>
	<%= Bean.getMetaContent() %>
	<%= Bean.getBottomFrameCSS() %>
</head>
<body>

<%
HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());

String pageFormName = "CLUB_EVENT_WAREHOUSE";
String tagGiftssEdit = "_GIFTS_EDIT";

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
	    		Bean.club_actionXML.getfieldTransl("h_add_warehouse", false),
				"Y",
				"Y") 
		%>
    <form action="../crm/club_event/warehouseupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return validateForm(formData)">
        <input type="hidden" name="action" value="add">
        <input type="hidden" name="process" value="yes">
    	<input type="hidden" name="type" value="general">
	<table <%=Bean.getTableDetailParam() %>>
		<tr>
			<td><%= Bean.logisticXML.getfieldTransl("name_lg_type", false) %></td><td><input type="text" name="name_lg_type" size="60" value="<%=Bean.getLGTypeName("GIFT") %>" readonly="readonly" class="inputfield-ro"></td>
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
			<td><%= Bean.logisticXML.getfieldTransl("gifts_count", false) %></td><td><input type="text" name="object_count" size="20" value="" class="inputfield"></td>
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
				<%=Bean.getSubmitButtonAjax("../crm/club_event/warehouseupdate.jsp") %>
				<%=Bean.getResetButton() %>
				<% if ("add".equalsIgnoreCase(action)) { %>
					<%=Bean.getGoBackButton("../crm/club_event/warehouses.jsp") %>
				<% } else { %>
					<%=Bean.getGoBackButton("../crm/club_event/warehousespecs.jsp?id=" + id) %>
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
    		String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_LG_CARD.add_action(" + 
    			"?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";

			String[] pParam = new String [14];

			pParam[0] = "GIFT";
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
			<%= Bean.getCallResultParam("INSERT", callSQL, pParam, "../crm/club_event/warehousespecs.jsp?id=" , "../crm/club_event/warehouses.jsp") %>
			<% 	

    	} else if (action.equalsIgnoreCase("remove")) { 
    		
    		String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_LG_CARD.delete_action(?,?)}";

			String[] pParam = new String [14];

			pParam[0] = id;

		 	%>
			<%= Bean.getCallResultParam("DELETE", callSQL, pParam, "../crm/club_event/warehouses.jsp" , "") %>
			
			<% 	

    	} else if (action.equalsIgnoreCase("edit")) { 
    		
    		String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_LG_CARD.update_action("+
	 			"?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";

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
			<%= Bean.getCallResultParam("UPDATE", callSQL, pParam, "../crm/club_event/warehousespecs.jsp?id=" + id, "") %>
			
			<% 	

    	} else { %> 
    		<%= Bean.getUnknownActionText(action) %><% 
    	}
	} else {
    	%> <%= Bean.getUnknownProcessText(process) %> <%
	}

} else if (type.equalsIgnoreCase("gift")) {
	if (process.equalsIgnoreCase("no"))
		/* вибираємо тип дії (добавити, видалити...)*/
		{
		   /*  --- Добавити запис --- */
	    	if (action.equalsIgnoreCase("add")) {
	    		
		        %>
		<script>
			var formData = new Array (
				new Array ('cd_gift', 'varchar2', 1),
				new Array ('desc_gift', 'varchar2', 1),
				new Array ('cd_currency', 'varchar2', 1),
				new Array ('cost_one_gift', 'varchar2', 1),
				new Array ('count_gifts', 'varchar2', 1)
			);
		</script> 
			<%= Bean.getOperationTitle(
		    		Bean.club_actionXML.getfieldTransl("h_gift_add", false),
					"Y",
					"N") 
			%>
	    <form action="../crm/club_event/warehouseupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return validateForm(formData)">
	        <input type="hidden" name="action" value="add">
	        <input type="hidden" name="process" value="yes">
	    	<input type="hidden" name="type" value="gift">
	    	<input type="hidden" name="id" value="<%=id %>">
		<table <%=Bean.getTableDetailParam() %>>
			<tr>
				<td><%= Bean.club_actionXML.getfieldTransl("cd_gift", true) %></td><td><input type="text" name="cd_gift" size="20" value="" class="inputfield"></td>
			</tr>
			<tr>
				<td><%= Bean.club_actionXML.getfieldTransl("desc_gift", true) %></td><td><input type="text" name="desc_gift" size="70" value="" class="inputfield"></td>
			</tr>
			<tr>
				<td><%= Bean.club_actionXML.getfieldTransl("cd_currency", true) %></td> <td><select name="cd_currency" class="inputfield"><%= Bean.getCurrencyOptions("", true) %></select></td>
			</tr>
			<tr>
				<td><%= Bean.club_actionXML.getfieldTransl("cost_one_gift", true) %></td><td><input type="text" name="cost_one_gift" size="20" value="" class="inputfield"></td>
			</tr>
			<tr>
				<td><%= Bean.club_actionXML.getfieldTransl("count_gifts", true) %></td><td><input type="text" name="count_gifts" size="20" value="" class="inputfield"></td>
			</tr>
	 		<tr>
				<td colspan="6" align="center">
					<%=Bean.getSubmitButtonAjax("../crm/club_event/warehouseupdate.jsp") %>
					<%=Bean.getResetButton() %>
					<%=Bean.getGoBackButton("../crm/club_event/warehousespecs.jsp?id="+id) %>
				</td>
			</tr>

		</table>
	</form>

		        <%
	    	} else if (action.equalsIgnoreCase("add_list")) {
	    		
		        %>
			<%= Bean.getOperationTitle(
		    		Bean.club_actionXML.getfieldTransl("h_add_gift_select", false),
					"Y",
					"N") 
			%>
	    <%
    	bcLGObject lgGift = new bcLGObject("GIFT", id);
	    
	    String l_gift_page	= Bean.getDecodeParam(parameters.get("gift_page"));
	    Bean.pageCheck(pageFormName + tagGiftssEdit, l_gift_page);
	    String l_gift_page_beg = Bean.getFirstRowNumber(pageFormName + tagGiftssEdit);
	    String l_gift_page_end = Bean.getLastRowNumber(pageFormName + tagGiftssEdit);

	    String tagGiftsFind = "_GIFTS_FIND_DET";
		String find_gift	= Bean.getDecodeParam(parameters.get("find_gift"));
		find_gift	 		= Bean.checkFindString(pageFormName + tagGiftsFind, find_gift, l_gift_page);
	    
	    

    	%>
		<script type="text/javascript">
		 	function CheckCB(Element) {
				myCheck = true;
		
				thisCheckBoxes = document.getElementsByTagName('input');
				for (i = 1; i < thisCheckBoxes.length; i++) { 
					myName = thisCheckBoxes[i].name;
					if (myName.substr(0,4) == 'chb_'){
						myCheck = myCheck && thisCheckBoxes[i].checked;
					}
				}
				if (document.getElementById('mainCheck')) {
					document.getElementById('mainCheck').checked = myCheck;
				}
			}
			function CheckAll(Element,Name) {
				thisCheckBoxes = document.getElementsByTagName('input');
				for (i = 1; i < thisCheckBoxes.length; i++) { 
					myName = thisCheckBoxes[i].name;
					
					if (myName.substr(0,4) == Name){
							thisCheckBoxes[i].checked = document.getElementById('mainCheck').checked;
					}
				}
			}
			CheckCB();
		</script>
		<table <%=Bean.getTableMenuParam() %>>
		<tr>
		<%= Bean.getFindHTML("find_gift", find_gift, "../crm/club_event/warehouseupdate.jsp?id=" + id + "&type=gift&process=no&action=add_list&gift_page=1&") %>
		<!-- Вывод страниц -->
		<%= Bean.getPagesHTML(pageFormName + tagGiftssEdit, "../crm/club_event/warehouseupdate.jsp?id=" + id + "&type=gift&process=no&action=add_list&", "gift_page") %>
		</tr>
		</table>
		<%=lgGift.getGiftsEditListHTML2(find_gift, l_gift_page_beg, l_gift_page_end) %>

		        <%
	    	} else {
	    	    %> <%= Bean.getUnknownActionText(action) %><%
	    	}
		
		} else if (process.equalsIgnoreCase("yes"))	{
	    	
			String
	    		cd_gift				= Bean.getDecodeParam(parameters.get("cd_gift")),
	    		desc_gift			= Bean.getDecodeParam(parameters.get("desc_gift")),
	    		cd_currency			= Bean.getDecodeParam(parameters.get("cd_currency")),
	    		cost_one_gift		= Bean.getDecodeParam(parameters.get("cost_one_gift")),
	    		count_gifts			= Bean.getDecodeParam(parameters.get("count_gifts"));

	    	if (action.equalsIgnoreCase("add")) { 
	    		String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_CLUB_ACTION.add_gift_to_warehouse2(" +
					"?,?,?,?,?,?,?,?)}";

				String[] pParam = new String [6];

				pParam[0] = id;
				pParam[1] = cd_gift;
				pParam[2] = desc_gift;
				pParam[3] = cd_currency;
				pParam[4] = cost_one_gift;
				pParam[5] = count_gifts;
			
			 	%>
				<%= Bean.getCallResultParam("INSERT", callSQL, pParam, "../crm/club_event/warehousespecs.jsp?id=" + id + "&id_lg_gift=", "") %>
				<% 	

	        } else if (action.equalsIgnoreCase("add_list")) { %> 
	        
				<%= Bean.form_messageXML.getfieldTransl("processing_update", false) %>
			   	<% 
		 
				String l_paramCount	= Bean.getDecodeParam(parameters.get("rowcount"));

			   	String[] results = new String[2];
		    	String fullResult = "0";
		    	String fullResultMessage = "";
			   	
		    	String checkBox = "";
		    	String idGift = "";
		    	String nameGift = "";
		    	String currencyGift = "";
		    	String costGift = "";
		    	String countGift = "";
		    	
		    	String callSQL = "";
				String resultInt = "";
				String resultId = "";
		 		String resultMessage = "";
		 		
		 		String[] pParam = new String [6];
			
				int i;
				for (i=1;i<=Integer.parseInt(l_paramCount);i++) {
					checkBox = Bean.getDecodeParam(parameters.get("chb_"+i));
					if (!(checkBox==null || "".equalsIgnoreCase(checkBox))) {
						idGift = Bean.getDecodeParam(parameters.get("id_"+i));
						nameGift = Bean.getDecodeParam(parameters.get("name_"+i));
						currencyGift = Bean.getDecodeParam(parameters.get("currency_"+i));
						costGift = Bean.getDecodeParam(parameters.get("cost_"+i));
						countGift = Bean.getDecodeParam(parameters.get("count_"+i));
						%>
						
						<%
						
						callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_CLUB_ACTION.add_gift_to_warehouse(" +
							"?,?,?,?,?,?,?,?)}";
						
						pParam[0] = id;
						pParam[1] = idGift;
						pParam[2] = nameGift;
						pParam[3] = currencyGift;
						pParam[4] = costGift;
						pParam[5] = countGift;
			
						results = Bean.myCallFunctionParam(callSQL, pParam, 3);
						resultInt = results[0];
						resultId = results[1];
						resultMessage = results[2];
						
						%>
						
						<%
						
						if (!("0".equalsIgnoreCase(resultInt))) { 
							fullResult = resultInt;
							fullResultMessage = fullResultMessage + ", " + resultMessage;
						}
					}
				}
				%>
		  	    <%=Bean.showCallResult(
		   	    		callSQL, 
		   	    		fullResult, 
		   	    		fullResultMessage, 
		   	    		"../crm/club_event/warehousespecs.jsp?id=" + id, 
		   	    		"../crm/club_event/warehousespecs.jsp?id=" + id, 
		   	    		Bean.form_messageXML.getfieldTransl("save_error", false)) %>
		   		<% 

	    	} else if (action.equalsIgnoreCase("remove")) { 
	    		
	    		String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_CLUB_ACTION.delete_gift_from_warehouse(?,?)}";

				String[] pParam = new String [1];

				pParam[0] = line;

			 	%>
				<%= Bean.getCallResultParam("DELETE", callSQL, pParam, "../crm/club_event/warehousespecs.jsp?id=" + id, "") %>
				
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
