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
String id				= Bean.getDecodeParam(parameters.get("id")); 
String id_lg_record		= Bean.getDecodeParam(parameters.get("id_lg_record")); 
String type				= Bean.getDecodeParam(parameters.get("type")); 
String action			= Bean.getDecodeParam(parameters.get("action")); 
String process			= Bean.getDecodeParam(parameters.get("process"));

if (id==null || ("".equalsIgnoreCase(id))) id="empty";
if (id_lg_record==null || ("".equalsIgnoreCase(id_lg_record))) id_lg_record="empty";
if (type==null || ("".equalsIgnoreCase(type))) type="empty";
if (action==null || ("".equalsIgnoreCase(action))) action="empty";
if (process==null || ("".equalsIgnoreCase(process))) process="empty";

if (type.equalsIgnoreCase("general")) {
	if (process.equalsIgnoreCase("no"))
		/* вибираємо тип дії (добавити, видалити...)*/
		{
		   /*  --- Добавити запис --- */
	    	if (action.equalsIgnoreCase("add")) {
	    		
	    		bcLGObject lgGift = new bcLGObject("GIFT", id_lg_record);
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
	    <form action="../crm/club_event/warehouseupdateupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return validateForm(formData)">
	        <input type="hidden" name="action" value="add">
	        <input type="hidden" name="process" value="yes">
	    	<input type="hidden" name="type" value="general">
	    	<input type="hidden" name="id_lg_record" value="<%=id_lg_record %>">
		<table <%=Bean.getTableDetailParam() %>>
			<tr>
				<td><%= Bean.logisticXML.getfieldTransl("id_lg_record", false) %>
					<%= Bean.getGoToClubEventWarehouseLink(lgGift.getValue("ID_LG_RECORD")) %>
				</td><td><input type="text" name="name_gift" size="20" value="<%=lgGift.getValue("ID_LG_RECORD") %> - <%=lgGift.getValue("ACTION_DATE_FRMT") %>" readonly="readonly" class="inputfield-ro"></td>
			</tr>
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
					<%=Bean.getSubmitButtonAjax("../crm/club_event/warehousegiftupdate.jsp") %>
					<%=Bean.getResetButton() %>
					<%=Bean.getGoBackButton("../crm/club_event/warehousegiftspecs.jsp?id="+id) %>
				</td>
			</tr>

		</table>
	</form>

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

				pParam[0] = id_lg_record;
				pParam[1] = cd_gift;
				pParam[2] = desc_gift;
				pParam[3] = cd_currency;
				pParam[4] = cost_one_gift;
				pParam[5] = count_gifts;
			
			 	%>
				<%= Bean.getCallResultParam("INSERT", callSQL, pParam, "../crm/club_event/warehousegiftspecs.jsp?id=", "") %>
				<% 	

	        } else if (action.equalsIgnoreCase("edit")) { 
		    	String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_CLUB_ACTION.update_gift_into_warehouse(" +
					"?,?,?,?,?,?)}";

				String[] pParam = new String [5];

				pParam[0] = id;
				pParam[1] = desc_gift;
				pParam[2] = cd_currency;
				pParam[3] = cost_one_gift;
				pParam[4] = count_gifts;
			
			 	%>
				<%= Bean.getCallResultParam("UPDATE", callSQL, pParam, "../crm/club_event/warehousegiftspecs.jsp?id=" + id, "") %>
				<% 	
	
	    	} else if (action.equalsIgnoreCase("remove")) { 
	    		
	    		String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_CLUB_ACTION.delete_gift_from_warehouse(?,?)}";

				String[] pParam = new String [1];

				pParam[0] = id;

			 	%>
				<%= Bean.getCallResultParam("DELETE", callSQL, pParam, "../crm/club_event/warehousespecs.jsp?id=" + id_lg_record, "") %>
				
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
