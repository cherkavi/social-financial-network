<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcCRMBean"  />


<%@page import="bc.objects.bcGiftObject"%>
<%@page import="java.util.HashMap"%>
<%@page import="bc.objects.bcClubShortObject"%><html>

<%= Bean.getLogOutScript(request) %>

<head>
	<%= Bean.getMetaContent() %>
	<%= Bean.getBottomFrameCSS() %>
</head>

<body>
<%
HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());

String pageFormName = "CLUB_EVENT_GIFTS";

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
				new Array ('cd_gift', 'varchar2', 1),
				new Array ('name_gift', 'varchar2', 1),
				new Array ('cd_gift_type', 'varchar2', 1),
				new Array ('name_club', 'varchar2', 1)
		);
	</script>
		<%= Bean.getOperationTitle(
				Bean.club_actionXML.getfieldTransl("h_gift_add", false),
				"Y",
				"Y") 
		%>
        <form action="../crm/club_event/giftupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return validateForm(formData)">
	        <input type="hidden" name="type" value="general">
	        <input type="hidden" name="action" value="add">
	        <input type="hidden" name="process" value="yes">
		<table <%=Bean.getTableDetailParam() %>>
		<tr>
			<td><%= Bean.club_actionXML.getfieldTransl("cd_gift", true) %> </td><td><input type="text" name="cd_gift" size="30" value="" class="inputfield"></td>
 		    <td><%= Bean.clubXML.getfieldTransl("club", true) %>
				<%= Bean.getGoToClubLink(club.getValue("ID_CLUB")) %>
			</td>
		  	<td>
				<%=Bean.getWindowFindClub("club", club.getValue("ID_CLUB"), club.getValue("SNAME_CLUB"), "25") %>
	  		</td>
		</tr>
        <tr>
			<td><%= Bean.club_actionXML.getfieldTransl("name_gift", true) %> </td><td><input type="text" name="name_gift" size="70" value="" class="inputfield"></td>
			<td colspan="2">&nbsp;</td>
		</tr>
		<tr>
			<td><%= Bean.club_actionXML.getfieldTransl("cd_gift_type", true) %></td> <td><select name="cd_gift_type" class="inputfield"><%= Bean.getGiftTypeOptions("", false) %></select> </td>
			<td colspan="2">&nbsp;</td>
		</tr>
		<tr>
			<td colspan="6" align="center">
				<%=Bean.getSubmitButtonAjax("../crm/club_event/giftupdate.jsp") %>
				<%=Bean.getResetButton() %>
				<% if ("add".equalsIgnoreCase(action)) { %>
					<%=Bean.getGoBackButton("../crm/club_event/gifts.jsp") %>
				<% } else { %>
					<%=Bean.getGoBackButton("../crm/club_event/giftspecs.jsp?id=" + id) %>
				<% } %>
			</td>
		</tr>
	</table>
	</form>

	        <%
    	} else {
    	    %> <%= Bean.getUnknownActionText(action) %><%
    	}
	
	} else if (process.equalsIgnoreCase("yes")) {
    
		String
			cd_gift			= Bean.getDecodeParam(parameters.get("cd_gift")),
			name_gift		= Bean.getDecodeParam(parameters.get("name_gift")),
    		cd_gift_type 	= Bean.getDecodeParam(parameters.get("cd_gift_type")),
    		id_club		 	= Bean.getDecodeParam(parameters.get("id_club"));

    	if (action.equalsIgnoreCase("add")) { 
    		
    		String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_GIFT.add_gift("+
    			"?,?,?,?,?,?)}";

    		String[] pParam = new String [4];
    				
    		pParam[0] = cd_gift;
    		pParam[1] = name_gift;
    		pParam[2] = cd_gift_type;
    		pParam[3] = id_club;
	
		 	%>
			<%= Bean.getCallResultParam("INSERT", callSQL, pParam, "../crm/club_event/giftspecs.jsp?id=" , "../crm/club_event/gifts.jsp") %>
			<% 	
   
    	} else if (action.equalsIgnoreCase("remove")) {

	   		String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_GIFT.delete_gift(?,?)}";

    		String[] pParam = new String [1];
    				
    		pParam[0] = id;

		 	%>
			<%= Bean.getCallResultParam("DELETE", callSQL, pParam, "../crm/club_event/gifts.jsp" , "") %>
			<% 	
	    
    	} else if (action.equalsIgnoreCase("edit")) { %> 
	        <% 
	        String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_GIFT.update_gift(" + 
	        	"?,?,?,?,?)}";

	    	String[] pParam = new String [4];
	    				
	    	pParam[0] = id;
	    	pParam[1] = cd_gift;
	    	pParam[2] = name_gift;
	    	pParam[3] = cd_gift_type;
	
		 	%>
			<%= Bean.getCallResultParam("UPDATE", callSQL, pParam, "../crm/club_event/giftspecs.jsp?id=" + id, "") %>
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
