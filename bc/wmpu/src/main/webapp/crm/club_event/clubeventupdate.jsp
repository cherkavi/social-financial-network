<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcCRMBean"  />

<%@page import="bc.objects.bcClubActionObject"%>
<%@page import="bc.objects.bcClubActionGiftObject"%>
<%@page import="bc.objects.bcClubActionWinnerObject"%>
<%@page import="bc.objects.bcClubActionEstimateObject"%>
<%@page import="java.util.HashMap"%>
<%@page import="bc.objects.bcClubShortObject"%>


<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Set"%>
<%@page import="java.util.Iterator"%>
<%@page import="bc.objects.bcClubActionGivenScheduleObject"%>
<%@page import="bc.objects.bcNatPrsGiftObject"%><html>

<%= Bean.getLogOutScript(request) %>

<head>
	<%= Bean.getMetaContent() %>
	<%= Bean.getBottomFrameCSS() %>
</head>

<body>
<%
HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());

String pageFormName = "CLUB_EVENT_EVENT";
String tagAllGiftsAdd = "_ALL_GIFTS_ADD";
String tagAllGiftsFind = "_ALL_GIFTS_FIND";

Bean.setJspPageForTabName(pageFormName);

request.setCharacterEncoding("UTF-8");

String type	= Bean.getDecodeParam(parameters.get("type"));
String id		= Bean.getDecodeParam(parameters.get("id")); 
String action	= Bean.getDecodeParam(parameters.get("action"));
String process	= Bean.getDecodeParam(parameters.get("process"));
 
if (id==null || ("".equalsIgnoreCase(id))) id="empty";
if (type==null || ("".equalsIgnoreCase(type))) type="empty";
if (action==null || ("".equalsIgnoreCase(action))) action="empty";
if (process==null || ("".equalsIgnoreCase(process))) process="empty";

String l_gift_page	= Bean.getDecodeParam(parameters.get("gift_page"));
Bean.pageCheck(pageFormName + tagAllGiftsAdd, l_gift_page);
String l_gift_page_beg = Bean.getFirstRowNumber(pageFormName + tagAllGiftsAdd);
String l_gift_page_end = Bean.getLastRowNumber(pageFormName + tagAllGiftsAdd);

String gifts_find 	= Bean.getDecodeParam(parameters.get("gifts_find"));
gifts_find 	= Bean.checkFindString(pageFormName + tagAllGiftsFind, gifts_find, l_gift_page);

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
			new Array ('cd_club_event_type', 'varchar2', 1),
			new Array ('name_club_event', 'varchar2', 1),
			new Array ('cd_club_event_state', 'varchar2', 1),
			new Array ('name_club', 'varchar2', 1),
			new Array ('date_beg', 'varchar2', 1)
		);
	</script>
		<%= Bean.getOperationTitle(
				Bean.club_actionXML.getfieldTransl("h_action_add", false),
				"Y",
				"Y") 
		%>
        <form action="../crm/club_event/clubeventupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return validateForm(formData)">
	        <input type="hidden" name="type" value="general">
	        <input type="hidden" name="action" value="add">
	        <input type="hidden" name="process" value="yes">
		<table <%=Bean.getTableDetailParam() %>>
        <tr>
			<td><%= Bean.club_actionXML.getfieldTransl("name_club_event_type", true) %></td> <td><select name="cd_club_event_type"  class="inputfield"><%= Bean.getClubActionTypeOptions("REGULAR", false) %></select> </td>
 		    <td><%= Bean.clubXML.getfieldTransl("club", true) %>
				<%= Bean.getGoToClubLink(club.getValue("ID_CLUB")) %>
			</td>
		  	<td>
				<%=Bean.getWindowFindClub("club", club.getValue("ID_CLUB"), club.getValue("SNAME_CLUB"), "25") %>
		  	</td>
		</tr>
        <tr>
			<td><%= Bean.club_actionXML.getfieldTransl("name_club_event", true) %> </td><td><input type="text" name="name_club_event" size="64" value="" class="inputfield"></td>
			<td><%= Bean.club_actionXML.getfieldTransl("name_club_event_state", true) %></td> <td><select name="cd_club_event_state"  class="inputfield"><%= Bean.getClubActionStateOptions("OPERATING", false) %></select> </td>
		</tr>
		<tr>
			<td rowspan="3"><%= Bean.club_actionXML.getfieldTransl("desc_action_club", false) %></td> <td rowspan="3"><textarea name="desc_action_club" cols="60" rows="3" class="inputfield"></textarea> </td>
			<td><%= Bean.club_actionXML.getfieldTransl("date_beg", true) %> </td><td><%=Bean.getCalendarInputField("date_beg", Bean.getSysDate(), "10") %></td>
		</tr>
		<tr>
			<td><%= Bean.club_actionXML.getfieldTransl("date_end", false) %> </td><td><%=Bean.getCalendarInputField("date_end", "", "10") %></td>
		</tr>
		<tr>
			<td colspan="2">&nbsp;</td>
		</tr>
		<tr>
			<td colspan="6" align="center">
				<%=Bean.getSubmitButtonAjax("../crm/club_event/clubeventupdate.jsp") %>
				<%=Bean.getResetButton() %>
				<% if ("add".equalsIgnoreCase(action)) { %>
				<%=Bean.getGoBackButton("../crm/club_event/clubevent.jsp") %>
				<% } else { %>
					<%=Bean.getGoBackButton("../crm/club_event/clubeventspecs.jsp?id=" + id) %>
				<% } %>	
			</td>
		</tr>
	</table>
	</form>
		<%= Bean.getCalendarScript("date_beg", false) %>
		<%= Bean.getCalendarScript("date_end", false) %>

	        <%
    	} else {
    	    %> <%= Bean.getUnknownActionText(action) %><%
    	}
	
	} else if (process.equalsIgnoreCase("yes")) {
    
		String
    		name_club_event				= Bean.getDecodeParam(parameters.get("name_club_event")),
    		cd_club_event_type 			= Bean.getDecodeParam(parameters.get("cd_club_event_type")),
    		cd_club_event_state 		= Bean.getDecodeParam(parameters.get("cd_club_event_state")),
    		desc_action_club 			= Bean.getDecodeParam(parameters.get("desc_action_club")),
    		date_beg		 			= Bean.getDecodeParam(parameters.get("date_beg")),
    		date_end		 			= Bean.getDecodeParam(parameters.get("date_end")),
    		LUD			 				= Bean.getDecodeParam(parameters.get("LUD")),
    		id_club		 				= Bean.getDecodeParam(parameters.get("id_club"));

    	if (action.equalsIgnoreCase("add")) { 
    		
    		String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_CLUB_ACTION.add_action(" +
    			"?,?,?,?,?,?,?,?,?,?)}";
	
	       	String[] pParam = new String [8];
				
			pParam[0] = name_club_event;
			pParam[1] = desc_action_club;
			pParam[2] = cd_club_event_type;
			pParam[3] = cd_club_event_state;
			pParam[4] = date_beg;
			pParam[5] = date_end;
			pParam[6] = id_club;
			pParam[7] = Bean.getDateFormat();
			
		 	%>
			<%= Bean.getCallResultParam("INSERT", callSQL, pParam, "../crm/club_event/clubeventspecs.jsp?id=" , "../crm/club_event/clubevent.jsp") %>
			<% 	
   
    	} else if (action.equalsIgnoreCase("remove")) {

	   		String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_CLUB_ACTION.delete_action(?,?)}";

	   		String[] pParam = new String [1];
			
	       	pParam[0] = id;
	       	
		 	%>
			<%= Bean.getCallResultParam("DELETE", callSQL, pParam, "../crm/club_event/clubevent.jsp" , "") %>
			<% 	
	    
    	} else if (action.equalsIgnoreCase("edit")) { %> 
	        <% 
	        String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_CLUB_ACTION.update_action(" + 
				"?,?,?,?,?,?,?,?,?,?)}";
	       		
	       	String[] pParam = new String [9];
				
	       	pParam[0] = id;
			pParam[1] = name_club_event;
			pParam[2] = desc_action_club;
			pParam[3] = cd_club_event_type;
			pParam[4] = cd_club_event_state;
			pParam[5] = date_beg;
			pParam[6] = date_end;
			pParam[7] = LUD;
			pParam[8] = Bean.getDateFormat();

		 	%>
			<%= Bean.getCallResultParam("UPDATE", callSQL, pParam,"../crm/club_event/clubeventspecs.jsp?id=" + id, "") %>
			<% 	
	     
    	} else { %> 
    		<%= Bean.getUnknownActionText(action) %><% 
    	}
	} else {%> 
		<%= Bean.getUnknownProcessText(process) %> <%
  	}


} else if (type.equalsIgnoreCase("gifts")) {
	
	String id_club_event_gift			= Bean.getDecodeParam(parameters.get("id_club_event_gift"));
	
	  if (process.equalsIgnoreCase("no"))
		  /* вибираємо тип дії (добавити, видалити...)*/
			{
			   /*  --- Добавити запис --- */
		if (action.equalsIgnoreCase("add")) {%> 

			<%= Bean.getOperationTitle(
					Bean.club_actionXML.getfieldTransl("h_gift_add", false),
					"Y",
					"N") 
			%>
			<script>
				var formData = new Array (
					new Array ('cd_club_event_gift', 'varchar2', 1),
					new Array ('name_club_event_gift', 'varchar2', 1),
					new Array ('is_active', 'varchar2', 1)
				);
				function check_event(elem_id) {
					document.getElementById("event_type_up_categories_bon").disabled=true;
					document.getElementById("ge_up_bon_category_number").disabled=true;
					document.getElementById("ge_up_bon_category_number").setAttribute('class', 'inputfield-ro');
					document.getElementById("event_type_up_categories_disc").disabled=true;
					document.getElementById("ge_up_disc_category_number").disabled=true;
					document.getElementById("ge_up_disc_category_number").setAttribute('class', 'inputfield-ro');
					document.getElementById("event_type_write_off_bons").disabled=true;
					document.getElementById("ge_write_off_bon_value").disabled=true;
					document.getElementById("ge_write_off_bon_value").setAttribute('class', 'inputfield-ro');
					document.getElementById("event_type_write_off_purse").disabled=true;
					document.getElementById("event_type_write_off_purse_type").disabled=true;
					document.getElementById("event_type_write_off_purse_type").setAttribute('class', 'inputfield-ro');
					document.getElementById("ge_write_off_purse_value").disabled=true;
					document.getElementById("ge_write_off_purse_value").setAttribute('class', 'inputfield-ro');
					document.getElementById("event_type_add_goods_bons").disabled=true;
					document.getElementById("ge_add_good_bon_value").disabled=true;
					document.getElementById("ge_add_good_bon_value").setAttribute('class', 'inputfield-ro');
					document.getElementById("event_type_add_goods_purse").disabled=true;
					document.getElementById("event_type_add_goods_purse_type").disabled=true;
					document.getElementById("event_type_add_goods_purse_type").setAttribute('class', 'inputfield-ro');
					document.getElementById("ge_add_good_purse_value").disabled=true;
					document.getElementById("ge_add_good_purse_value").setAttribute('class', 'inputfield-ro');
					if (elem_id=="event_type_up_categories") {
						document.getElementById("event_type_up_categories_bon").disabled=false;
						document.getElementById("ge_up_bon_category_number").disabled=false;
						document.getElementById("ge_up_bon_category_number").setAttribute('class', 'inputfield');
						document.getElementById("event_type_up_categories_disc").disabled=false;
						document.getElementById("ge_up_disc_category_number").disabled=false;
						document.getElementById("ge_up_disc_category_number").setAttribute('class', 'inputfield');
					}
					if (elem_id=="event_type_write_off_goods") {
						document.getElementById("event_type_write_off_bons").disabled=false;
						document.getElementById("ge_write_off_bon_value").disabled=false;
						document.getElementById("ge_write_off_bon_value").setAttribute('class', 'inputfield');
						document.getElementById("event_type_write_off_purse").disabled=false;
						document.getElementById("event_type_write_off_purse_type").disabled=false;
						document.getElementById("event_type_write_off_purse_type").setAttribute('class', 'inputfield');
						document.getElementById("ge_write_off_purse_value").disabled=false;
						document.getElementById("ge_write_off_purse_value").setAttribute('class', 'inputfield');
					}
					if (elem_id=="event_type_add_goods") {
						document.getElementById("event_type_add_goods_bons").disabled=false;
						document.getElementById("ge_add_good_bon_value").disabled=false;
						document.getElementById("ge_add_good_bon_value").setAttribute('class', 'inputfield');
						document.getElementById("event_type_add_goods_purse").disabled=false;
						document.getElementById("event_type_add_goods_purse_type").disabled=false;
						document.getElementById("event_type_add_goods_purse_type").setAttribute('class', 'inputfield');
						document.getElementById("ge_add_good_purse_value").disabled=false;
						document.getElementById("ge_add_good_purse_value").setAttribute('class', 'inputfield');
					}
				}
			</script>
	        <form action="../crm/club_event/clubeventupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return validateForm(formData)">
		        <input type="hidden" name="type" value="gifts">
		        <input type="hidden" name="action" value="add">
		        <input type="hidden" name="process" value="yes">
        		<input type="hidden" name="id" value="<%= id %>">
			<table <%=Bean.getTableDetailParam() %>>
				<tr>
					<td><%= Bean.club_actionXML.getfieldTransl("id_club_event", false) %> </td><td><input type="text" name="id_club_event" size="20" value="<%= id %>" readonly="readonly" class="inputfield-ro"></td>
					<td><%= Bean.club_actionXML.getfieldTransl("cd_given_method_type", true) %> </td>
					<td><%=Bean.getSelectBeginHTML("cd_given_method_type", Bean.club_actionXML.getfieldTransl("cd_given_method_type", false)) %>
						<%=Bean.getSelectOptionHTML("MANUAL", "MANUAL", Bean.club_actionXML.getfieldTransl("cd_given_method_type_manual", false)) %>
						<%=Bean.getSelectOptionHTML("MANUAL", "CARD_REQUEST", Bean.club_actionXML.getfieldTransl("cd_given_method_type_card_request", false)) %>
					<%=Bean.getSelectEndHTML() %></td>
				</tr>
		        <tr>
					<td><%= Bean.club_actionXML.getfieldTransl("gift_from_catalog", true) %> </td>
					<td>
						<%=Bean.getWindowGifts("gift", "", "40") %>
					</td>			
					<td colspan="2"><b><%= Bean.club_actionXML.getfieldTransl("cd_given_event_type", true) %></b></td>
				</tr>
		        <tr>
					<td><%= Bean.club_actionXML.getfieldTransl("cd_club_event_gift", true) %></td><td><input type="text" name="cd_club_event_gift" size="20" value="" class="inputfield"></td>
					<td colspan="2"><input type="radio" name="event_type" id="event_type_none" value="NONE" class="inputfield" CHECKED onclick="check_event(this.id)"><b><label class="checbox_label" for="event_type_none"><%= Bean.club_actionXML.getfieldTransl("event_type_none", false) %></label></b></td>
				</tr>
		        <tr>
					<td><%= Bean.club_actionXML.getfieldTransl("name_club_event_gift", true) %></td><td><input type="text" name="name_club_event_gift" size="50" value="" class="inputfield"></td>
					<td colspan="2"><input type="radio" name="event_type" id="event_type_up_categories" value="UP_CATEGORIES" class="inputfield" onclick="check_event(this.id)"><b><label class="checbox_label" for="event_type_up_categories"><%= Bean.club_actionXML.getfieldTransl("event_type_up_categories", false) %></label></b></td>
				</tr>
		        <tr>
					<td><%= Bean.club_actionXML.getfieldTransl("is_active", true) %> </td><td><select name="is_active"  class="inputfield"><%= Bean.getMeaningFromLookupNameOptions("YES_NO", "Y", false) %></select></td>
					<td colspan="2">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="checkbox" name="event_type_up_categories_bon" id="event_type_up_categories_bon" value="Y" class="inputfield"><label class="checbox_label" for="event_type_up_categories_bon"><%= Bean.club_actionXML.getfieldTransl("event_type_up_categories_bon", false) %></label>, номер категории <input type="text" name="ge_up_bon_category_number" id="ge_up_bon_category_number" size="10" value="" class="inputfield"></td>
				</tr>
		        <tr>
					<td><%= Bean.club_actionXML.getfieldTransl("count_gift_all", false) %></td><td><input type="text" name="count_gift_all" size="20" value="" class="inputfield"></td>
					<td colspan="2">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="checkbox" name="event_type_up_categories_disc" id="event_type_up_categories_disc" value="Y" class="inputfield"><label class="checbox_label" for="event_type_up_categories_disc"><%= Bean.club_actionXML.getfieldTransl("event_type_up_categories_disc", false) %></label>, номер категории <input type="text" name="ge_up_disc_category_number" id="ge_up_disc_category_number" size="10" value="" class="inputfield"></td>
				</tr>
		        <tr>
					<td colspan="2">&nbsp;</td>
					<td colspan="2"><input type="radio" name="event_type" id="event_type_write_off_goods" value="WRITE_OFF_GOODS" class="inputfield" onclick="check_event(this.id)"><b><label class="checbox_label" for="event_type_write_off_goods"><%= Bean.club_actionXML.getfieldTransl("event_type_write_off_goods", false) %></label></b></td>
				</tr>
		        <tr>
					<td colspan="2">&nbsp;</td>
					<td colspan="2">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="radio" name="event_type_write_off_goods" id="event_type_write_off_bons" value="WRITE_OFF_BONS" class="inputfield"><label class="checbox_label" for="event_type_write_off_bons"><%= Bean.club_actionXML.getfieldTransl("event_type_write_off_bons", false) %></label>, сумма <input type="text" name="ge_write_off_bon_value" id="ge_write_off_bon_value" size="10" value="" class="inputfield"></td>
				</tr>
		        <tr>
					<td colspan="2">&nbsp;</td>
					<td colspan="2">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="radio" name="event_type_write_off_goods" id="event_type_write_off_purse" value="WRITE_OFF_GOODS_FROM_PURSE" class="inputfield"><label class="checbox_label" for="event_type_write_off_purse"><%= Bean.club_actionXML.getfieldTransl("event_type_write_off_purse", false) %></label>&nbsp;<select name="event_type_write_off_purse_type" id="event_type_write_off_purse_type"  class="inputfield" title="<%= Bean.club_actionXML.getfieldTransl("cd_card_purse_type", false) %>"><%= Bean.getClubCardPurseTypeOptions("", true) %></select>, сумма <input type="text" name="ge_write_off_purse_value" id="ge_write_off_purse_value" size="10" value="" class="inputfield"></td>
				</tr>
		        <tr>
					<td colspan="2">&nbsp;</td>
					<td colspan="2"><input type="radio" name="event_type" id="event_type_add_goods" value="ADD_GOODS" class="inputfield" onclick="check_event(this.id)"><b><label class="checbox_label" for="event_type_add_goods"><%= Bean.club_actionXML.getfieldTransl("event_type_add_goods", false) %></label></b></td>
				</tr>
				<tr>
					<td colspan="2">&nbsp;</td>
					<td colspan="2">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="radio" name="event_type_add_goods_value" id="event_type_add_goods_bons" value="ADD_BONS" class="inputfield"><label class="checbox_label" for="event_type_add_goods_bons"><%= Bean.club_actionXML.getfieldTransl("event_type_add_goods_bons", false) %></label>, сумма <input type="text" name="ge_add_good_bon_value" id="ge_add_good_bon_value" size="10" value="" class="inputfield"></td>
				</tr>
				<tr>
					<td colspan="2">&nbsp;</td>
					<td colspan="2">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="radio" name="event_type_add_goods_value" id="event_type_add_goods_purse" value="ADD_GOODS_TO_PURSE" class="inputfield"><label class="checbox_label" for="event_type_add_goods_purse"><%= Bean.club_actionXML.getfieldTransl("event_type_add_goods_purse", false) %></label>&nbsp;<select name="event_type_add_goods_purse_type" id="event_type_add_goods_purse_type" class="inputfield" title="<%= Bean.club_actionXML.getfieldTransl("cd_card_purse_type", false) %>"><%= Bean.getClubCardPurseTypeOptions("", true) %></select>, сумма <input type="text" name="ge_add_good_purse_value" id="ge_add_good_purse_value" size="10" value="" class="inputfield"></td>
				</tr>
				<tr>
					<td colspan="6" align="center">
						<%=Bean.getSubmitButtonAjax("../crm/club_event/clubeventupdate.jsp") %>
						<%=Bean.getResetButton() %>
						<%=Bean.getGoBackButton("../crm/club_event/clubeventspecs.jsp?id=" + id) %>
					</td>
				</tr>
			</table>
			</form>

        <%
		} else if (action.equalsIgnoreCase("addall")) {
			bcClubActionObject club_action = new bcClubActionObject(id);
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
	function CheckAll(Element) {
		thisCheckBoxes = document.getElementsByTagName('input');
		for (i = 1; i < thisCheckBoxes.length; i++) { 
			myName = thisCheckBoxes[i].name;
			
			if (myName.substr(0,4) == 'chb_'){
					thisCheckBoxes[i].checked = document.getElementById('mainCheck').checked;
			}
		}
	}
	CheckCB();
</script>

			<%= Bean.getOperationTitle(
					Bean.club_actionXML.getfieldTransl("h_gift_add", false),
					"Y",
					"N") 
			%>
			<table <%=Bean.getTableMenuFilter() %>>
		<tr>
			<%= Bean.getFindHTML("gifts_find", gifts_find, "../crm/club_event/clubeventupdate.jsp?id=" + id + "&type=gifts&process=no&action=addall&gift_page=1") %>
			<!-- Вывод страниц -->
			<%= Bean.getPagesHTML(pageFormName + tagAllGiftsAdd, "../crm/club_event/clubeventupdate.jsp?id=" + id + "&type=gifts&process=no&action=addall&", "gift_page") %>
			</tr>
		</table>
		<%=club_action.getClubActionAddAllGiftsHTML(gifts_find, l_gift_page_beg,l_gift_page_end) %>
		<%
		} else if (action.equalsIgnoreCase("edit")) {
		    		
		 	bcClubActionGiftObject gift = new bcClubActionGiftObject(id_club_event_gift);
		    
		    %> 

			<script>
				var formData = new Array (
					new Array ('cd_club_event_gift', 'varchar2', 1),
					new Array ('name_club_event_gift', 'varchar2', 1),
					new Array ('is_active', 'varchar2', 1)
				);
				function check_event(elem_id) {
					document.getElementById("event_type_up_categories_bon").disabled=true;
					document.getElementById("ge_up_bon_category_number").disabled=true;
					document.getElementById("ge_up_bon_category_number").setAttribute('class', 'inputfield-ro');
					document.getElementById("event_type_up_categories_disc").disabled=true;
					document.getElementById("ge_up_disc_category_number").disabled=true;
					document.getElementById("ge_up_disc_category_number").setAttribute('class', 'inputfield-ro');
					document.getElementById("event_type_write_off_bons").disabled=true;
					document.getElementById("ge_write_off_bon_value").disabled=true;
					document.getElementById("ge_write_off_bon_value").setAttribute('class', 'inputfield-ro');
					document.getElementById("event_type_write_off_purse").disabled=true;
					document.getElementById("event_type_write_off_purse_type").disabled=true;
					document.getElementById("event_type_write_off_purse_type").setAttribute('class', 'inputfield-ro');
					document.getElementById("ge_write_off_purse_value").disabled=true;
					document.getElementById("ge_write_off_purse_value").setAttribute('class', 'inputfield-ro');
					document.getElementById("event_type_add_goods_bons").disabled=true;
					document.getElementById("ge_add_good_bon_value").disabled=true;
					document.getElementById("ge_add_good_bon_value").setAttribute('class', 'inputfield-ro');
					document.getElementById("event_type_add_goods_purse").disabled=true;
					document.getElementById("event_type_add_goods_purse_type").disabled=true;
					document.getElementById("event_type_add_goods_purse_type").setAttribute('class', 'inputfield-ro');
					document.getElementById("ge_add_good_purse_value").disabled=true;
					document.getElementById("ge_add_good_purse_value").setAttribute('class', 'inputfield-ro');
					if (elem_id=="event_type_up_categories") {
						document.getElementById("event_type_up_categories_bon").disabled=false;
						document.getElementById("ge_up_bon_category_number").disabled=false;
						document.getElementById("ge_up_bon_category_number").setAttribute('class', 'inputfield');
						document.getElementById("event_type_up_categories_disc").disabled=false;
						document.getElementById("ge_up_disc_category_number").disabled=false;
						document.getElementById("ge_up_disc_category_number").setAttribute('class', 'inputfield');
					}
					if (elem_id=="event_type_write_off_goods") {
						document.getElementById("event_type_write_off_bons").disabled=false;
						document.getElementById("ge_write_off_bon_value").disabled=false;
						document.getElementById("ge_write_off_bon_value").setAttribute('class', 'inputfield');
						document.getElementById("event_type_write_off_purse").disabled=false;
						document.getElementById("event_type_write_off_purse_type").disabled=false;
						document.getElementById("event_type_write_off_purse_type").setAttribute('class', 'inputfield');
						document.getElementById("ge_write_off_purse_value").disabled=false;
						document.getElementById("ge_write_off_purse_value").setAttribute('class', 'inputfield');
					}
					if (elem_id=="event_type_add_goods") {
						document.getElementById("event_type_add_goods_bons").disabled=false;
						document.getElementById("ge_add_good_bon_value").disabled=false;
						document.getElementById("ge_add_good_bon_value").setAttribute('class', 'inputfield');
						document.getElementById("event_type_add_goods_purse").disabled=false;
						document.getElementById("event_type_add_goods_purse_type").disabled=false;
						document.getElementById("event_type_add_goods_purse_type").setAttribute('class', 'inputfield');
						document.getElementById("ge_add_good_purse_value").disabled=false;
						document.getElementById("ge_add_good_purse_value").setAttribute('class', 'inputfield');
					}
				}
			</script>
		<% 
			String event_type_none_checked = "";
			String event_type_up_categories_checked = "";
			String event_type_write_off_goods_checked = "";
			String event_type_add_goods_checked = "";
			if ("NONE".equalsIgnoreCase(gift.getValue("CD_GIVEN_EVENT_TYPE"))) {
				event_type_none_checked = "CHECKED";
				%>
				<script>
				check_event("event_type_none");
				</script>
				<%
			} else if ("UP_CATEGORIES".equalsIgnoreCase(gift.getValue("CD_GIVEN_EVENT_TYPE"))) {
				event_type_up_categories_checked = "CHECKED";
				%>
				<script>
				check_event("event_type_up_categories");
				</script>
				<%
			} else if ("WRITE_OFF_GOODS".equalsIgnoreCase(gift.getValue("CD_GIVEN_EVENT_TYPE"))) {
				event_type_write_off_goods_checked = "CHECKED";
				%>
				<script>
				check_event("event_type_write_off_goods");
				</script>
				<%
			} else if ("ADD_GOODS".equalsIgnoreCase(gift.getValue("CD_GIVEN_EVENT_TYPE"))) {
				event_type_add_goods_checked = "CHECKED";
				%>
				<script>
				check_event("event_type_add_goods");
				</script>
				<%
			}
		%>

			<%= Bean.getOperationTitle(
					Bean.club_actionXML.getfieldTransl("h_gift_edit", false),
					"Y",
					"N") 
			%>
	        <form action="../crm/club_event/clubeventupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return validateForm(formData)">
		        <input type="hidden" name="type" value="gifts">
		        <input type="hidden" name="action" value="edit">
		        <input type="hidden" name="process" value="yes">
		        <input type="hidden" name="id" value="<%= id %>">
		        <input type="hidden" name="id_club_event_gift" value="<%= id_club_event_gift %>">
		        <input type="hidden" name="LUD" value="<%= gift.getValue("LUD") %>">
			<table <%=Bean.getTableDetailParam() %>>
				<tr>
					<td><%= Bean.club_actionXML.getfieldTransl("id_club_event", false) %> </td><td><input type="text" name="id_club_event" size="20" value="<%= id %>" readonly="readonly" class="inputfield-ro"></td>
					<td><%= Bean.club_actionXML.getfieldTransl("cd_given_method_type", true) %> </td>
					<td><%=Bean.getSelectBeginHTML("cd_given_method_type", Bean.club_actionXML.getfieldTransl("cd_given_method_type", false)) %>
						<%=Bean.getSelectOptionHTML(gift.getValue("CD_GIVEN_METHOD_TYPE"), "MANUAL", Bean.club_actionXML.getfieldTransl("cd_given_method_type_manual", false)) %>
						<%=Bean.getSelectOptionHTML(gift.getValue("CD_GIVEN_METHOD_TYPE"), "CARD_REQUEST", Bean.club_actionXML.getfieldTransl("cd_given_method_type_card_request", false)) %>
					<%=Bean.getSelectEndHTML() %></td>
				</tr>
		        <tr>
					<td><%= Bean.club_actionXML.getfieldTransl("gift_from_catalog", false) %></td><td><input type="text" name="name_gift" size="50" value="<%= gift.getValue("CD_GIFT") %>: <%= gift.getValue("NAME_GIFT") %>" readonly="readonly" class="inputfield-ro"></td>
					<td colspan="2"><b><%= Bean.club_actionXML.getfieldTransl("cd_given_event_type", true) %></b></td>
				</tr>
		        <tr>
					<td><%= Bean.club_actionXML.getfieldTransl("cd_club_event_gift", true) %></td><td><input type="text" name="cd_club_event_gift" size="20" value="<%= gift.getValue("CD_CLUB_EVENT_GIFT") %>" class="inputfield"></td>
					<td colspan="2"><input type="radio" name="event_type" id="event_type_none" value="NONE" class="inputfield" <%=event_type_none_checked %> onclick="check_event(this.id)"><b><label class="checbox_label" for="event_type_none"><%= Bean.club_actionXML.getfieldTransl("event_type_none", false) %></label></b></td>
				</tr>
		        <tr>
					<td><%= Bean.club_actionXML.getfieldTransl("name_club_event_gift", true) %></td><td><input type="text" name="name_club_event_gift" size="50" value="<%= gift.getValue("NAME_CLUB_EVENT_GIFT") %>" class="inputfield"></td>
					<td colspan="2"><input type="radio" name="event_type" id="event_type_up_categories" value="UP_CATEGORIES" class="inputfield" <%=event_type_up_categories_checked %> onclick="check_event(this.id)"><b><label class="checbox_label" for="event_type_up_categories"><%= Bean.club_actionXML.getfieldTransl("event_type_up_categories", false) %></label></b></td>
				</tr>
		        <tr>
					<td><%= Bean.club_actionXML.getfieldTransl("is_active", true) %> </td><td><select name="is_active"  class="inputfield"><%= Bean.getMeaningFromLookupNameOptions("YES_NO", gift.getValue("IS_ACTIVE"), false) %></select></td>
					<td colspan="2">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="checkbox" name="event_type_up_categories_bon" id="event_type_up_categories_bon" value="Y" <%if ("Y".equalsIgnoreCase(gift.getValue("GE_IS_UP_BON_CATEGORY"))) { %>CHECKED<%} %> class="inputfield"><label class="checbox_label" for="event_type_up_categories_bon"><%= Bean.club_actionXML.getfieldTransl("event_type_up_categories_bon", false) %></label>, номер категории <input type="text" name="ge_up_bon_category_number" id="ge_up_bon_category_number" size="10" value="<%= gift.getValue("GE_UP_BON_CATEGORY_NUMBER") %>" class="inputfield"></td>
				</tr>
		        <tr>
					<td><%= Bean.club_actionXML.getfieldTransl("count_gift_all", false) %></td><td><input type="text" name="count_gift_all" size="20" value="<%= gift.getValue("COUNT_GIFT_ALL") %>" class="inputfield"></td>
					<td colspan="2">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="checkbox" name="event_type_up_categories_disc" id="event_type_up_categories_disc" value="Y" <%if ("Y".equalsIgnoreCase(gift.getValue("GE_IS_UP_DISC_CATEGORY"))) { %>CHECKED<%} %>  class="inputfield"><label class="checbox_label" for="event_type_up_categories_disc"><%= Bean.club_actionXML.getfieldTransl("event_type_up_categories_disc", false) %></label>, номер категории <input type="text" name="ge_up_disc_category_number" id="ge_up_disc_category_number" size="10" value="<%= gift.getValue("GE_UP_DISC_CATEGORY_NUMBER") %>" class="inputfield"></td>
				</tr>
		        <tr>
					<td><%= Bean.club_actionXML.getfieldTransl("count_gift_reserved", false) %></td><td><input type="text" name="count_gift_reserved" size="20" value="<%= gift.getValue("COUNT_GIFT_RESERVED") %>" readonly="readonly" class="inputfield-ro"></td>
					<td colspan="2"><input type="radio" name="event_type" id="event_type_write_off_goods" value="WRITE_OFF_GOODS" class="inputfield" <%=event_type_write_off_goods_checked %> onclick="check_event(this.id)"><b><label class="checbox_label" for="event_type_write_off_goods"><%= Bean.club_actionXML.getfieldTransl("event_type_write_off_goods", false) %></label></b></td>
				</tr>
		        <tr>
					<td><%= Bean.club_actionXML.getfieldTransl("count_gift_given", false) %></td><td><input type="text" name="count_gift_given" size="20" value="<%= gift.getValue("COUNT_GIFT_GIVEN") %>" readonly="readonly" class="inputfield-ro"></td>
					<td colspan="2">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="radio" name="event_type_write_off_goods" id="event_type_write_off_bons" value="WRITE_OFF_BONS" <%if ("WRITE_OFF_BONS".equalsIgnoreCase(gift.getValue("GE_WRITE_OFF_TYPE"))) { %>CHECKED<%} %> class="inputfield"><label class="checbox_label" for="event_type_write_off_bons"><%= Bean.club_actionXML.getfieldTransl("event_type_write_off_bons", false) %></label>, сумма <input type="text" name="ge_write_off_bon_value" id="ge_write_off_bon_value" size="10" value="<%= gift.getValue("GE_WRITE_OFF_BON_VALUE_FRMT") %>" class="inputfield"></td>
				</tr>
		        <tr>
					<td><%= Bean.club_actionXML.getfieldTransl("count_gift_remain", false) %></td><td><input type="text" name="count_gift_remain" size="20" value="<%= gift.getValue("COUNT_GIFT_REMAIN") %>" readonly="readonly" class="inputfield-ro"></td>
					<td colspan="2">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="radio" name="event_type_write_off_goods" id="event_type_write_off_purse" value="WRITE_OFF_GOODS_FROM_PURSE" <%if ("WRITE_OFF_GOODS_FROM_PURSE".equalsIgnoreCase(gift.getValue("GE_WRITE_OFF_TYPE"))) { %>CHECKED<%} %> class="inputfield"><label class="checbox_label" for="event_type_write_off_purse"><%= Bean.club_actionXML.getfieldTransl("event_type_write_off_purse", false) %></label>&nbsp;<select name="event_type_write_off_purse_type" id="event_type_write_off_purse_type"  class="inputfield" title="<%= Bean.club_actionXML.getfieldTransl("cd_card_purse_type", false) %>"><%= Bean.getClubCardPurseTypeOptions(gift.getValue("GE_WRITE_OFF_CD_PURSE_TYPE"), true) %></select>, сумма <input type="text" name="ge_write_off_purse_value" id="ge_write_off_purse_value" size="10" value="<%= gift.getValue("GE_WRITE_OFF_PURSE_VALUE_FRMT") %>" class="inputfield"></td>
				</tr>
		        <tr>
					<td colspan="2">&nbsp;</td>
					<td colspan="2"><input type="radio" name="event_type" id="event_type_add_goods" value="ADD_GOODS" class="inputfield" <%=event_type_add_goods_checked %> onclick="check_event(this.id)"><b><label class="checbox_label" for="event_type_add_goods"><%= Bean.club_actionXML.getfieldTransl("event_type_add_goods", false) %></label></b></td>
				</tr>
				<tr>
					<td colspan="2">&nbsp;</td>
					<td colspan="2">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="radio" name="event_type_add_goods_value" id="event_type_add_goods_bons" value="ADD_BONS" <%if ("ADD_BONS".equalsIgnoreCase(gift.getValue("GE_ADD_GOOD_TYPE"))) { %>CHECKED<%} %> class="inputfield"><label class="checbox_label" for="event_type_add_goods_bons"><%= Bean.club_actionXML.getfieldTransl("event_type_add_goods_bons", false) %></label>, сумма <input type="text" name="ge_add_good_bon_value" id="ge_add_good_bon_value" size="10" value="<%= gift.getValue("GE_ADD_GOOD_BON_VALUE_FRMT") %>" class="inputfield"></td>
				</tr>
				<tr>
					<td colspan="2">&nbsp;</td>
					<td colspan="2">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="radio" name="event_type_add_goods_value" id="event_type_add_goods_purse" value="ADD_GOODS_TO_PURSE" <%if ("ADD_GOODS_TO_PURSE".equalsIgnoreCase(gift.getValue("GE_ADD_GOOD_TYPE"))) { %>CHECKED<%} %> class="inputfield"><label class="checbox_label" for="event_type_add_goods_purse"><%= Bean.club_actionXML.getfieldTransl("event_type_add_goods_purse", false) %></label>&nbsp;<select name="event_type_add_goods_purse_type" id="event_type_add_goods_purse_type" class="inputfield" title="<%= Bean.club_actionXML.getfieldTransl("cd_card_purse_type", false) %>"><%= Bean.getClubCardPurseTypeOptions(gift.getValue("GE_ADD_GOOD_CD_PURSE_TYPE"), true) %></select>, сумма <input type="text" name="ge_add_good_purse_value" id="ge_add_good_purse_value" size="10" value="<%= gift.getValue("GE_ADD_GOOD_PURSE_VALUE_FRMT") %>" class="inputfield"></td>
				</tr>
				<%=	Bean.getCreationAndMoficationRecordFields(
						gift.getValue(Bean.getCreationDateFieldName()),
						gift.getValue("CREATED_BY"),
						gift.getValue(Bean.getLastUpdateDateFieldName()),
						gift.getValue("LAST_UPDATE_BY")
					) %>
				<tr>
					<td colspan="6" align="center">
						<%=Bean.getSubmitButtonAjax("../crm/club_event/clubeventupdate.jsp") %>
						<%=Bean.getResetButton() %>
						<%=Bean.getGoBackButton("../crm/club_event/clubeventspecs.jsp?id=" + id) %>
					</td>
				</tr>
			</table>

			</form>
			
			<br><%
		} else {
		    %> <%= Bean.getUnknownActionText(action) %><%
		}
		  
	} else if (process.equalsIgnoreCase("yes")){
		    
		String
			id_gift							= Bean.getDecodeParam(parameters.get("id_gift")),
			cd_club_event_gift				= Bean.getDecodeParam(parameters.get("cd_club_event_gift")),
			name_club_event_gift			= Bean.getDecodeParam(parameters.get("name_club_event_gift")),
		    count_gift_all					= Bean.getDecodeParam(parameters.get("count_gift_all")),
		    is_active 						= Bean.getDecodeParam(parameters.get("is_active")),
		    cd_given_method_type 			= Bean.getDecodeParam(parameters.get("cd_given_method_type")),
		    cd_given_event_type 			= Bean.getDecodeParam(parameters.get("event_type")),
		    ge_is_up_bon_category 			= Bean.getDecodeParam(parameters.get("event_type_up_categories_bon")),
		    ge_up_bon_category_number 		= Bean.getDecodeParam(parameters.get("ge_up_bon_category_number")),
		    ge_is_up_disc_category 			= Bean.getDecodeParam(parameters.get("event_type_up_categories_disc")),
		    ge_up_disc_category_number 		= Bean.getDecodeParam(parameters.get("ge_up_disc_category_number")),
		    ge_write_off_type 				= Bean.getDecodeParam(parameters.get("event_type_write_off_goods")),
		    ge_write_off_bon_value 			= Bean.getDecodeParam(parameters.get("ge_write_off_bon_value")),
		    ge_write_off_cd_purse_type 		= Bean.getDecodeParam(parameters.get("event_type_write_off_purse_type")),
		    ge_write_off_purse_value 		= Bean.getDecodeParam(parameters.get("ge_write_off_purse_value")),
		    ge_add_good_type 				= Bean.getDecodeParam(parameters.get("event_type_add_goods_value")),
		    ge_add_good_bon_value 			= Bean.getDecodeParam(parameters.get("ge_add_good_bon_value")),
		    ge_add_good_cd_purse_type 		= Bean.getDecodeParam(parameters.get("event_type_add_goods_purse_type")),
		    ge_add_good_purse_value 		= Bean.getDecodeParam(parameters.get("ge_add_good_purse_value")),
		    LUD								= Bean.getDecodeParam(parameters.get("LUD"));

		if (action.equalsIgnoreCase("add")) { 
				
			String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_CLUB_ACTION.add_gift("+
				"?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
			String[] pParam = new String [20];
				
			pParam[0] = id;
			pParam[1] = id_gift;
			pParam[2] = cd_club_event_gift;
			pParam[3] = name_club_event_gift;
			pParam[4] = count_gift_all;
			pParam[5] = is_active;
			pParam[6] = cd_given_method_type;
			pParam[7] = cd_given_event_type;
			pParam[8] = ge_is_up_bon_category;
			pParam[9] = ge_up_bon_category_number;
			pParam[10] = ge_is_up_disc_category;
			pParam[11] = ge_up_disc_category_number;
			pParam[12] = ge_write_off_type;
			pParam[13] = ge_write_off_bon_value;
			pParam[14] = ge_write_off_cd_purse_type;
			pParam[15] = ge_write_off_purse_value;
			pParam[16] = ge_add_good_type;
			pParam[17] = ge_add_good_bon_value;
			pParam[18] = ge_add_good_cd_purse_type;
			pParam[19] = ge_add_good_purse_value;
		 	%>
			<%= Bean.getCallResultParam("INSERT", callSQL, pParam, "../crm/club_event/clubeventspecs.jsp?id=" + id + "&id_club_event_gift=", "") %>
			<% 	
		   
	    } else if (action.equalsIgnoreCase("addall")) {  %>
			
			<%= Bean.form_messageXML.getfieldTransl("processing_update", false) %>
			<%
			
			String l_paramCount	= Bean.getDecodeParam(parameters.get("rowcount"));
			
			String[] results = new String[2];
	    	String fullResult = "0";
	    	String fullResultMessage = "";
	    	
	    	String checkBox = "";
	    	String idGift = "";
	    	String cdGift = "";
	    	String nameGift = "";
	    	String countGift = "";
	    	
	    	String callSQL = "";
			String resultInt = "";
			String resultId = "";
	 		String resultMessage = "";
		
			int i;
			for (i=1;i<=Integer.parseInt(l_paramCount);i++) {
				checkBox = Bean.getDecodeParam(parameters.get("chb_"+i));
				if (!(checkBox==null || "".equalsIgnoreCase(checkBox))) {
					idGift = Bean.getDecodeParam(parameters.get("id_"+i));
					cdGift = Bean.getDecodeParam(parameters.get("cd_"+i));
					nameGift = Bean.getDecodeParam(parameters.get("name_"+i));
					countGift = Bean.getDecodeParam(parameters.get("count_"+i));
					%>
					
					<%
					String[] pParam = new String [20];
					
					callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_CLUB_ACTION.add_gift("+
						"?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
						
					pParam[0] = id;
					pParam[1] = idGift;
					pParam[2] = cdGift;
					pParam[3] = nameGift;
					pParam[4] = countGift;
					pParam[5] = "Y";
					pParam[6] = "MANUAL";
					pParam[7] = "NONE";
					pParam[8] = "";
					pParam[9] = "";
					pParam[10] = "";
					pParam[11] = "";
					pParam[12] = "";
					pParam[13] = "";
					pParam[14] = "";
					pParam[15] = "";
					pParam[16] = "";
					pParam[17] = "";
					pParam[18] = "";
					pParam[19] = "";
					
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
	   	    		"../crm/club_event/clubeventupdate.jsp?id=" + id + "&type=gifts&process=no&action=addall", 
	   	    		"../crm/club_event/clubeventupdate.jsp?id=" + id + "&type=gifts&process=no&action=addall", 
	   	    		Bean.form_messageXML.getfieldTransl("save_error", false)) %>
	   		<% 

		} else if (action.equalsIgnoreCase("remove")) {
			   
			String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_CLUB_ACTION.delete_gift(?,?)}";

			String[] pParam = new String [1];
			pParam[0] = id_club_event_gift;
		 	%>
			<%= Bean.getCallResultParam("DELETE", callSQL, pParam, "../crm/club_event/clubeventspecs.jsp?id=" + id, "") %>
			<% 	
			     
		} else if (action.equalsIgnoreCase("edit")) { 
			
			String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_CLUB_ACTION.update_gift(" +
				"?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
				
			String[] pParam = new String[20];
			pParam[0] = id_club_event_gift;
			pParam[1] = cd_club_event_gift;
			pParam[2] = name_club_event_gift;
			pParam[3] = count_gift_all;
			pParam[4] = is_active;
			pParam[5] = cd_given_method_type;
			pParam[6] = cd_given_event_type;
			pParam[7] = ge_is_up_bon_category;
			pParam[8] = ge_up_bon_category_number;
			pParam[9] = ge_is_up_disc_category;
			pParam[10] = ge_up_disc_category_number;
			pParam[11] = ge_write_off_type;
			pParam[12] = ge_write_off_bon_value;
			pParam[13] = ge_write_off_cd_purse_type;
			pParam[14] = ge_write_off_purse_value;
			pParam[15] = ge_add_good_type;
			pParam[16] = ge_add_good_bon_value;
			pParam[17] = ge_add_good_cd_purse_type;
			pParam[18] = ge_add_good_purse_value;
			pParam[19] = LUD;
	
		 	%>
			<%= Bean.getCallResultParam("UPDATE", callSQL, pParam, "../crm/club_event/clubeventspecs.jsp?id=" + id, "") %>
			<% 	
			     
		} else { %> 
			<%= Bean.getUnknownActionText(action) %><% 
		}
	} else {%> 
		<%= Bean.getUnknownProcessText(process) %> <%
	}

} else if (type.equalsIgnoreCase("request")) {

	  if (process.equalsIgnoreCase("no"))
		  /* вибираємо тип дії (добавити, видалити...)*/
			{
		  %>
			<script>
				var formData = new Array (
					new Array ('cd_nat_prs_gift_request_type', 'varchar2', 1),
					new Array ('cd_nat_prs_gift_request_state', 'varchar2', 1),
					new Array ('date_accept', 'varchar2', 1),
					new Array ('name_nat_prs', 'varchar2', 1)
				);
			</script>

		  <%
			   /*  --- Добавити запис --- */
		if (action.equalsIgnoreCase("add")) { 
		
			bcClubActionObject event = new bcClubActionObject(id);
		%> 

			<%= Bean.getOperationTitle(
					Bean.club_actionXML.getfieldTransl("h_winner_add", false),
					"Y",
					"N") 
			%>
	        <form action="../crm/club_event/clubeventupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return validateForm(formData)">
		        <input type="hidden" name="type" value="request">
		        <input type="hidden" name="action" value="add">
		        <input type="hidden" name="process" value="yes">
      			<input type="hidden" name="id" value="<%= id %>">
      			<input type="hidden" name="id_club_event" id="id_club_event" value="<%= id %>">
			<table <%=Bean.getTableDetailParam() %>>
				<tr>
					<td><%= Bean.club_actionXML.getfieldTransl("name_club_event", false) %> </td><td><input type="text" name="name_club_event" id="name_club_event" size="60" value="<%= event.getValue("NAME_CLUB_EVENT") %>" readonly="readonly" class="inputfield-ro"></td>
					<td colspan="2">&nbsp;</td>
				</tr>
	
				<tr>
					<td><%= Bean.club_actionXML.getfieldTransl("cd_nat_prs_gift_request_type", true) %></td><td><select name="cd_nat_prs_gift_request_type" class="inputfield"><%=Bean.getNatPrsGiftRequestTypeOptionsExclude("", "", true) %> </select></td>
					<td colspan="2">&nbsp;</td>
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
						<%=Bean.getWindowFindNatPrs("nat_prs", "", "", "50") %>
					</td>
					<td colspan="2">&nbsp;</td>
				</tr>
				<tr>
					<td><%= Bean.club_actionXML.getfieldTransl("text_request", false) %></td><td><textarea name="text_request" cols="56" rows="3" class="inputfield"></textarea></td>
					<td colspan="2">&nbsp;</td>
				</tr>
		
				<tr>
					<td colspan="6" align="center">
						<%=Bean.getSubmitButtonAjax("../crm/club_event/clubeventupdate.jsp") %>
						<%=Bean.getResetButton() %>
						<%=Bean.getGoBackButton("../crm/club_event/clubeventspecs.jsp?id=" + id) %>
					</td>
				</tr>
			</table>
			</form>

      <%
	   	} else {%> 
	   		<%= Bean.getUnknownActionText(action) %><%
		}
	} else if (process.equalsIgnoreCase("yes")) {
		    
		String
			id_nat_prs_gift_request			= Bean.getDecodeParam(parameters.get("id_nat_prs_gift_request")),
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
			pParam[4] = id;
			pParam[5] = text_request;
			pParam[6] = Bean.getDateFormat();

		 	%>
			<%= Bean.getCallResultParam("INSERT", callSQL, pParam, "../crm/club_event/clubeventspecs.jsp?id=" + id + "&id_nat_prs_gift_request=", "") %>
			<% 	
		   
		} else if (action.equalsIgnoreCase("remove")) { 
				
			String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_CLUB_ACTION.delete_nat_prs_gift_request(?,?)}";

			String[] pParam = new String [1];
			
			pParam[0] = id_nat_prs_gift_request;

		 	%>
			<%= Bean.getCallResultParam("DELETE", callSQL, pParam, "../crm/club_event/clubeventspecs.jsp?id=" + id, "") %>
			<% 	
		   
		} else { %> 
	    	<%= Bean.getUnknownActionText(action) %><% 
	    }
	} else {%> 
		<%= Bean.getUnknownProcessText(process) %> <%
	}
	  
} else if (type.equalsIgnoreCase("winner")) {

	  if (process.equalsIgnoreCase("no"))
		  /* вибираємо тип дії (добавити, видалити...)*/
			{
		  %>
			<script>
				var formData = new Array (
					new Array ('date_reserve', 'varchar2', 1),
					new Array ('name_nat_prs', 'varchar2', 1),
					new Array ('name_club_event_gift', 'varchar2', 1)
				);
			</script>

		  <%
			   /*  --- Добавити запис --- */
		if (action.equalsIgnoreCase("add")) { 
		
			bcClubActionObject event = new bcClubActionObject(id);
		%> 

			<%= Bean.getOperationTitle(
					Bean.club_actionXML.getfieldTransl("h_winner_add", false),
					"Y",
					"N") 
			%>
	        <form action="../crm/club_event/clubeventupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return validateForm(formData)">
		        <input type="hidden" name="type" value="winner">
		        <input type="hidden" name="action" value="add">
		        <input type="hidden" name="process" value="yes">
        		<input type="hidden" name="id" value="<%= id %>">
        		<input type="hidden" name="id_club_event" id="id_club_event" value="<%= id %>">
			<table <%=Bean.getTableDetailParam() %>>
				<tr>
					<td><%= Bean.club_actionXML.getfieldTransl("name_club_event", false) %> </td><td><input type="text" name="name_club_event" id="name_club_event" size="60" value="<%= event.getValue("NAME_CLUB_EVENT") %>" readonly="readonly" class="inputfield-ro"></td>
					<td colspan="2">&nbsp;</td>
				</tr>
	
		        <tr>
					<td><%= Bean.club_actionXML.getfieldTransl("date_reserve", true) %></td> <td><%=Bean.getCalendarInputField("date_reserve", Bean.getSysDate(), "10") %></td>
					<td colspan="2">&nbsp;</td>
				</tr>
				<tr>
				  	<td><%= Bean.club_actionXML.getfieldTransl("full_name", true) %></td>
					<td>
						<%=Bean.getWindowFindNatPrs("nat_prs", "", "", "50") %>
					</td>
					<td colspan="2">&nbsp;</td>
				</tr>
				<tr>
		 		    <td><%= Bean.club_actionXML.getfieldTransl("name_gift", true) %></td>
				  	<td>
						<%=Bean.getWindowFindClubActionGifts("club_event_gift", "'+document.getElementById('id_club_event').value+'", "", "", "50") %>
			  		</td>
					<td colspan="2">&nbsp;</td>
				</tr>
				<tr>
					<td><%= Bean.club_actionXML.getfieldTransl("basis_for_gift", false) %></td> <td><textarea name="basis_for_gift" cols="56" rows="3" class="inputfield"></textarea></td>
					<td colspan="2">&nbsp;</td>
				</tr>
				<tr>
					<td colspan="6" align="center">
						<%=Bean.getSubmitButtonAjax("../crm/club_event/clubeventupdate.jsp") %>
						<%=Bean.getResetButton() %>
						<%=Bean.getGoBackButton("../crm/club_event/clubeventspecs.jsp?id=" + id) %>
					</td>
				</tr>
			</table>
			</form>

        <%
	   	} else {%> 
	   		<%= Bean.getUnknownActionText(action) %><%
		}
	} else if (process.equalsIgnoreCase("yes")) {
		    
		String
			id_nat_prs_gift					= Bean.getDecodeParam(parameters.get("id_nat_prs_gift")),
			id_nat_prs						= Bean.getDecodeParam(parameters.get("id_nat_prs")),
			date_reserve 					= Bean.getDecodeParam(parameters.get("date_reserve")),
			id_club_event_gift				= Bean.getDecodeParam(parameters.get("id_club_event_gift"));

	    if (action.equalsIgnoreCase("add")) { 
				
	    	String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_CLUB_ACTION.add_nat_prs_gift("+  
	        	"?,?,?,?,?,?,?,?,?)}";

	    	String[] pParam = new String [7];
	    			
	    	pParam[0] = id_nat_prs;
	    	pParam[1] = date_reserve;
	    	pParam[2] = id_club_event_gift;
	    	pParam[3] = "";
	    	pParam[4] = "";
	    	pParam[5] = "";
	    	pParam[6] = Bean.getDateFormat();

		 	%>
			<%= Bean.getCallResultParam("INSERT", callSQL, pParam, "../crm/club_event/clubeventspecs.jsp?id=" + id + "&id_nat_prs_gift=", "") %>
			<% 	
		   
		} else if (action.equalsIgnoreCase("remove")) { 
				
			String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_CLUB_ACTION.delete_winner(?,?)}";

	    	String[] pParam = new String [1];
	    			
	    	pParam[0] = id_nat_prs_gift;

		 	%>
			<%= Bean.getCallResultParam("DELETE", callSQL, pParam, "../crm/club_event/clubeventspecs.jsp?id=" + id, "") %>
			<% 	
		   
		} else { %> 
	    	<%= Bean.getUnknownActionText(action) %><% 
	    }
	} else {%> 
		<%= Bean.getUnknownProcessText(process) %> <%
	}
	  
} else if (type.equalsIgnoreCase("estimate")) {
	
	String id_estimate	    = Bean.getDecodeParam(parameters.get("id_estimate"));
	
	  if (process.equalsIgnoreCase("no"))
		  /* вибираємо тип дії (добавити, видалити...)*/
			{
		  %>
			<script>
				var formData = new Array (
					new Array ('value_criterion', 'varchar2', 1),
					new Array ('id_club_event_estim_crit', 'varchar2', 1)
				);
			</script>

		  <%
			   /*  --- Добавити запис --- */
		if (action.equalsIgnoreCase("add")) {
		    		
			bcClubActionObject club_action = new bcClubActionObject(id);
			
			%> 

			<%= Bean.getOperationTitle(
					Bean.club_actionXML.getfieldTransl("h_estimate_add", false),
					"Y",
					"N") 
			%>
	        <form action="../crm/club_event/clubeventupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return validateForm(formData)">
		        <input type="hidden" name="type" value="estimate">
		        <input type="hidden" name="action" value="add">
		        <input type="hidden" name="process" value="yes">
        		<input type="hidden" name="id" value="<%= id %>">
			<table <%=Bean.getTableDetailParam() %>>
				<tr>
					<td><%= Bean.club_actionXML.getfieldTransl("name_club_event", true) %></td>	<td><input type="text" name="name_club_event" size="60" value="<%= club_action.getValue("NAME_CLUB_EVENT") %>" readonly="readonly" class="inputfield-ro"></td>
					<td colspan="2">&nbsp;</td>
				</tr>
		        <tr>
					<td valign="top"><%= Bean.club_actionXML.getfieldTransl("id_club_event_estim_crit", true) %> </td><td valign="top"><select name="id_club_event_estim_crit"  class="inputfield"><%= Bean.getEstimateCriterionsOptions("", false) %></select></td>
					<td colspan="2">&nbsp;</td>
				</tr>
		        <tr>
					<td valign="top"><%= Bean.club_actionXML.getfieldTransl("value_criterion", true) %></td><td valign="top"><textarea name="value_criterion" cols="56" rows="4" class="inputfield"></textarea></td>
					<td valign="top"><%= Bean.club_actionXML.getfieldTransl("note_action", false) %> </td><td valign="top"><textarea name="note_action" cols="56" rows="4" class="inputfield"></textarea></td>
				</tr>
				<tr>
					<td colspan="6" align="center">
						<%=Bean.getSubmitButtonAjax("../crm/club_event/clubeventupdate.jsp") %>
						<%=Bean.getResetButton() %>
						<%=Bean.getGoBackButton("../crm/club_event/clubeventspecs.jsp?id=" + id) %>
					</td>
				</tr>
			</table>
			</form>

        <%
		} else if (action.equalsIgnoreCase("edit")) {
		    		
			bcClubActionEstimateObject estimate = new bcClubActionEstimateObject(id_estimate);
		    		
		    %> 

			<%= Bean.getOperationTitle(
					Bean.club_actionXML.getfieldTransl("h_estimate_edit", false),
					"Y",
					"N") 
			%>
	        <form action="../crm/club_event/clubeventupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return validateForm(formData)">
		        <input type="hidden" name="type" value="estimate">
		        <input type="hidden" name="action" value="edit">
		        <input type="hidden" name="process" value="yes">
		        <input type="hidden" name="id" value="<%= id %>">
		        <input type="hidden" name="id_estimate" value="<%= id_estimate %>">
			<table <%=Bean.getTableDetailParam() %>>
				<tr>
					<td><%= Bean.club_actionXML.getfieldTransl("name_club_event", false) %></td>	<td><input type="text" name="name_club_event" size="60" value="<%= estimate.getValue("NAME_CLUB_EVENT") %>" readonly="readonly" class="inputfield-ro"></td>
					<td colspan="2">&nbsp;</td>
				</tr>
		        <tr>
					<td><%= Bean.club_actionXML.getfieldTransl("name_club_event_estim_crit", false) %></td><td valign="top"><input type="text" name="name_action_estim_criterion" size="60" value="<%= estimate.getValue("NAME_ACTION_ESTIM_CRITERION") %>" readonly="readonly" class="inputfield-ro"></td>
					<td colspan="2">&nbsp;</td>
				</tr>
		        <tr>
					<td valign="top"><%= Bean.club_actionXML.getfieldTransl("value_criterion", true) %></td><td valign="top"><textarea name="value_criterion" cols="56" rows="4" class="inputfield"><%=estimate.getValue("VALUE_CRITERION") %></textarea></td>
					<td valign="top"><%= Bean.club_actionXML.getfieldTransl("note_action", false) %> </td><td valign="top"><textarea name="note_action" cols="56" rows="4" class="inputfield"><%=estimate.getValue("NOTE_ACTION") %></textarea></td>
				</tr>
				<%=	Bean.getCreationAndMoficationRecordFields(
						estimate.getValue(Bean.getCreationDateFieldName()),
						estimate.getValue("CREATED_BY"),
						estimate.getValue(Bean.getLastUpdateDateFieldName()),
						estimate.getValue("LAST_UPDATE_BY")
					) %>
				<tr>
					<td colspan="6" align="center">
						<%=Bean.getSubmitButtonAjax("../crm/club_event/clubeventupdate.jsp") %>
						<%=Bean.getResetButton() %>
						<%=Bean.getGoBackButton("../crm/club_event/clubeventspecs.jsp?id=" + id) %>
					</td>
				</tr>
			</table>

			</form>
			
			<br><%
		} else { %> 
			<%= Bean.getUnknownActionText(action) %><%
		}
	} else if (process.equalsIgnoreCase("yes")) {
		    
		String
			id_club_event_estim_crit 	= Bean.getDecodeParam(parameters.get("id_club_event_estim_crit")),
	    	value_criterion 			= Bean.getDecodeParam(parameters.get("value_criterion")),
		    note_action 				= Bean.getDecodeParam(parameters.get("note_action"));

		if (action.equalsIgnoreCase("add")) { 
				
			String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_CLUB_ACTION.add_estimation(" +
				"?,?,?,?,?,?)}";

			String[] pParam = new String [4];
					
			pParam[0] = id;
			pParam[1] = id_club_event_estim_crit;
			pParam[2] = value_criterion;
			pParam[3] = note_action;

		 	%>
			<%=callSQL %>
			<%= Bean.getCallResultParam("INSERT", callSQL, pParam, "../crm/club_event/clubeventspecs.jsp?id=" + id + "&id_criterion=", "") %>
			<% 	
		   
		} else if (action.equalsIgnoreCase("remove")) {

			String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_CLUB_ACTION.delete_estimation(?,?)}";

			String[] pParam = new String [4];
						
			pParam[0] = id_estimate;

		 	%>
			<%= Bean.getCallResultParam("DELETE", callSQL, pParam, "../crm/club_event/clubeventspecs.jsp?id=" + id, "") %>
			<% 	
			     
		} else if (action.equalsIgnoreCase("edit")) { 
			        
			String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_CLUB_ACTION.update_estimation(" +
				"?,?,?,?)}";

			String[] pParam = new String [3];
						
			pParam[0] = id_estimate;
			pParam[1] = value_criterion;
			pParam[2] = note_action;

		 	%>
			<%= Bean.getCallResultParam("UPDATE", callSQL, pParam, "../crm/club_event/clubeventspecs.jsp?id=" + id, "") %>
			<% 	
		
		} else { %> 
			<%= Bean.getUnknownActionText(action) %><% 
		}
	} else { %> 
		<%= Bean.getUnknownProcessText(process) %> <%
	}
} else if (type.equalsIgnoreCase("given_schedule")) {

	String
		id_club_event_given_schedule	= Bean.getDecodeParam(parameters.get("id_club_event_given_schedule")),
		id_gifts_given_place			= Bean.getDecodeParam(parameters.get("id_gifts_given_place")),
		desc_gifts_given_place			= Bean.getDecodeParam(parameters.get("desc_gifts_given_place")),
		cd_given_day					= Bean.getDecodeParam(parameters.get("cd_given_day")),
		begin_given_hour				= Bean.getDecodeParam(parameters.get("begin_given_hour")),
		begin_given_min					= Bean.getDecodeParam(parameters.get("begin_given_min")),
		end_given_hour					= Bean.getDecodeParam(parameters.get("end_given_hour")),
		end_given_min					= Bean.getDecodeParam(parameters.get("end_given_min"));
	
	if (process.equalsIgnoreCase("no")) {
		if (action.equalsIgnoreCase("add")) {
		    		
			bcClubActionObject club_action = new bcClubActionObject(id);
			
			        %> 
		<script>
			var formData = new Array (
				new Array ('name_gifts_given_place', 'varchar2', 1),
				new Array ('cd_given_day', 'varchar2', 1)
			);
		</script>
			<%= Bean.getOperationTitle(
					Bean.club_actionXML.getfieldTransl("h_add_club_event_given_schedule", false),
					"Y",
					"Y") 
			%>
	        <form action="../crm/club_event/clubeventupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return validateForm(formData)">
		        <input type="hidden" name="type" value="given_schedule">
		        <input type="hidden" name="action" value="add">
		        <input type="hidden" name="process" value="yes">
		        <input type="hidden" name="id" value="<%=id %>">
			<table <%=Bean.getTableDetailParam() %>>
			<tr>
	 		    <td><%= Bean.club_actionXML.getfieldTransl("name_club_event", true) %></td>
			  	<td>
					<input type="text" name="name_club_event" size="70" value="<%= club_action.getValue("NAME_CLUB_EVENT") %>" readonly="readonly" class="inputfield-ro">
		  		</td>
				<td><%= Bean.club_actionXML.getfieldTransl("cd_given_day", true) %></td><td><select name="cd_given_day" class="inputfield"><%=Bean.getMeaningFromLookupNameOrderByNymberValueOptions("DAY_NAME", "", true) %> </select></td>
	 		</tr>
			<tr>
				<td><%= Bean.club_actionXML.getfieldTransl("gifts_given_place", true) %></td>
				<td>
					<%=Bean.getWindowFindServicePlace("gifts_given_place", "", "", "60") %>
				</td>
				<td><%= Bean.club_actionXML.getfieldTransl("given_period", false) %></td>
				<td>
					<%= Bean.commonXML.getfieldTransl("h_period_from", false) %>&nbsp;
					<select name="begin_given_hour" class="inputfield"><%= Bean.getMeaningFromLookupNameOrderByNymberValueOptions("HOURS","09", true) %></select>
					<select name="begin_given_min" class="inputfield"><%= Bean.getMeaningFromLookupNameOrderByNymberValueOptions("SECONDS","00", true) %></select>
					&nbsp;&nbsp;
					<%= Bean.commonXML.getfieldTransl("h_period_to", false) %>&nbsp;
					<select name="end_given_hour" class="inputfield"><%= Bean.getMeaningFromLookupNameOrderByNymberValueOptions("HOURS","18", true) %></select>
					<select name="end_given_min" class="inputfield"><%= Bean.getMeaningFromLookupNameOrderByNymberValueOptions("SECONDS","00", true) %></select>
				</td>			
			</tr>
			<tr>
				<td><%= Bean.club_actionXML.getfieldTransl("desc_gifts_given_place", false) %></td><td><textarea name="desc_gifts_given_place" cols="70" rows="3" class="inputfield"></textarea></td>
			</tr>
			<tr>
				<td colspan="6" align="center">
					<%=Bean.getSubmitButtonAjax("../crm/club_event/clubeventupdate.jsp") %>
					<%=Bean.getResetButton() %>
					<%=Bean.getGoBackButton("../crm/club_event/clubeventspecs.jsp?id=" + id) %>
				</td>
			</tr>
		</table>
		</form>

			        <%
		} else if (action.equalsIgnoreCase("edit")) {
		    		
			bcClubActionGivenScheduleObject schedule = new bcClubActionGivenScheduleObject(id_club_event_given_schedule);

			        %> 
		<script>
			var formData = new Array (
					new Array ('date_gifts_given', 'varchar2', 1)
			);
		</script>
			<%= Bean.getOperationTitle(
					Bean.club_actionXML.getfieldTransl("h_update_club_event_given_schedule", false),
					"Y",
					"Y")  
			%>
	        <form action="../crm/club_event/clubeventupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return validateForm(formData)">
		        <input type="hidden" name="type" value="given_schedule">
		        <input type="hidden" name="action" value="edit">
		        <input type="hidden" name="process" value="yes">
		        <input type="hidden" name="id" value="<%=id %>">
			<table <%=Bean.getTableDetailParam() %>>
		<tr>
			<td><%= Bean.club_actionXML.getfieldTransl("id_club_event_given_schedule", false) %></td><td><input type="text" name="id_club_event_given_schedule" size="20" value="<%= schedule.getValue("ID_CLUB_EVENT_GIVEN_SCHEDULE") %>" readonly="readonly" class="inputfield-ro"></td>
			<td><%= Bean.club_actionXML.getfieldTransl("cd_given_day", true) %></td><td><select name="cd_given_day" class="inputfield"><%=Bean.getMeaningFromLookupNameOrderByNymberValueOptions("DAY_NAME", schedule.getValue("CD_GIVEN_DAY"), true) %> </select></td>
		</tr>
		<tr>
			<td><%= Bean.club_actionXML.getfieldTransl("name_club_event", false) %>
				<%= Bean.getGoToClubEventLink(schedule.getValue("ID_CLUB_EVENT")) %>
			</td><td><input type="text" name="name_club_event" size="74" value="<%= schedule.getValue("NAME_CLUB_EVENT") %>" readonly="readonly" class="inputfield-ro"></td>
			<td><%= Bean.club_actionXML.getfieldTransl("given_period", false) %></td>
			<td>
				<%= Bean.commonXML.getfieldTransl("h_period_from", false) %>&nbsp;
				<select name="begin_given_hour" class="inputfield"><%= Bean.getMeaningFromLookupNameOrderByNymberValueOptions("HOURS",schedule.getValue("BEGIN_GIVEN_HOUR"), true) %></select>
				<select name="begin_given_min" class="inputfield"><%= Bean.getMeaningFromLookupNameOrderByNymberValueOptions("SECONDS",schedule.getValue("BEGIN_GIVEN_MIN"), true) %></select>
				&nbsp;&nbsp;
				<%= Bean.commonXML.getfieldTransl("h_period_to", false) %>&nbsp;
				<select name="end_given_hour" class="inputfield"><%= Bean.getMeaningFromLookupNameOrderByNymberValueOptions("HOURS",schedule.getValue("END_GIVEN_HOUR"), true) %></select>
				<select name="end_given_min" class="inputfield"><%= Bean.getMeaningFromLookupNameOrderByNymberValueOptions("SECONDS",schedule.getValue("END_GIVEN_MIN"), true) %></select>
			</td>			
		</tr>
		<tr>
			<td><%= Bean.club_actionXML.getfieldTransl("gifts_given_place", true) %>
				<%= Bean.getGoToClubEventGivenPlaceLink(schedule.getValue("ID_GIFTS_GIVEN_PLACE")) %>
			</td>
			<td>
				<%=Bean.getWindowFindServicePlace("gifts_given_place", schedule.getValue("ID_GIFTS_GIVEN_PLACE"), schedule.getValue("NAME_GIFTS_GIVEN_PLACE"), "60") %>
			</td>
		</tr>
		<tr>
			<td><%= Bean.club_actionXML.getfieldTransl("desc_gifts_given_place", false) %></td><td><textarea name="desc_gifts_given_place" cols="70" rows="3" class="inputfield"><%= schedule.getValue("DESC_GIFTS_GIVEN_PLACE") %></textarea></td>
		</tr>
		<%=	Bean.getCreationAndMoficationRecordFields(
				schedule.getValue(Bean.getCreationDateFieldName()),
				schedule.getValue("CREATED_BY"),
				schedule.getValue(Bean.getLastUpdateDateFieldName()),
				schedule.getValue("LAST_UPDATE_BY")
			) %>
		<tr>
			<td colspan="4" align="center">
				<%=Bean.getSubmitButtonAjax("../crm/club_event/clubeventupdate.jsp") %>
				<%=Bean.getResetButton() %>
				<%=Bean.getGoBackButton("../crm/club_event/clubeventspecs.jsp?id=" + id) %>
			</td>
		</tr>
		</table>
		</form>

			        <%
		} else {
		    %> <%= Bean.getUnknownActionText(action) %><%
		}
			
	} else if (process.equalsIgnoreCase("yes")) {
		    

	   	if (action.equalsIgnoreCase("add")) { 
		    		
	   		String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_CLUB_ACTION.add_action_given_schedule("+
	   			"?,?,?,?,?,?,?,?,?,?)}";

    		String[] pParam = new String [8];
    				
    		pParam[0] = id;
    		pParam[1] = id_gifts_given_place;
    		pParam[2] = desc_gifts_given_place;
    		pParam[3] = cd_given_day;
    		pParam[4] = begin_given_hour;
    		pParam[5] = begin_given_min;
    		pParam[6] = end_given_hour;
    		pParam[7] = end_given_min;
		
		 	%>
			<%= Bean.getCallResultParam("INSERT", callSQL, pParam, "../crm/club_event/clubeventspecs.jsp?id=" + id + "&id_club_event_given_schedule=" , "../crm/club_event/given_place.jsp") %>
			<% 	
		   
	   	} else if (action.equalsIgnoreCase("remove")) {

	   		String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_CLUB_ACTION.delete_action_given_schedule(?,?)}";

			String[] pParam = new String [1];
				
			pParam[0] = id_club_event_given_schedule;
			
			%>
			<%= Bean.getCallResultParam("DELETE", callSQL, pParam, "../crm/club_event/clubeventspecs.jsp?id=" + id , "") %>
			<% 	
	    
    	} else if (action.equalsIgnoreCase("edit")) { %> 
	        <% 
	        String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_CLUB_ACTION.update_action_given_schedule(" + 
	        	"?,?,?,?,?,?,?,?,?)}";

        	String[] pParam = new String [8];
        				
        	pParam[0] = id_club_event_given_schedule;
        	pParam[1] = id_gifts_given_place;
        	pParam[2] = desc_gifts_given_place;
        	pParam[3] = cd_given_day;
        	pParam[4] = begin_given_hour;
        	pParam[5] = begin_given_min;
        	pParam[6] = end_given_hour;
        	pParam[7] = end_given_min;
	
		 	%>
			<%= Bean.getCallResultParam("UPDATE", callSQL, pParam, "../crm/club_event/clubeventspecs.jsp?id=" + id, "") %>
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
