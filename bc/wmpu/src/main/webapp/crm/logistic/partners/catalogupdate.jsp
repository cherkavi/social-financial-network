<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcCRMBean"  />

<%= Bean.getLogOutScript(request) %>

<%@page import="java.util.HashMap"%>
<%@page import="bc.objects.bcClubShortObject"%><html>
<head>
	<%= Bean.getMetaContent() %>
	<%= Bean.getBottomFrameCSS() %>
</head>
<body>

<%
HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());

String pageFormName = "LOGISTIC_PARTNERS_CATALOG";

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
    	if (action.equalsIgnoreCase("add") || action.equalsIgnoreCase("add2")) {
    		
    		bcClubShortObject club = new bcClubShortObject(Bean.getCurrentClubID());

	        %>
	<script>
		var formData = new Array (
			new Array ('name_lg_production', 'varchar2', 1),
			new Array ('name_club', 'varchar2', 1)
		);
	</script> 
		<%= Bean.getOperationTitle(
	    		Bean.logisticXML.getfieldTransl("h_production_add", false),
				"Y",
				"Y") 
		%>
    <form action="../crm/logistic/partners/catalogupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return validateForm(formData)">
        <input type="hidden" name="action" value="add">
        <input type="hidden" name="process" value="yes">
    	<input type="hidden" name="type" value="general">
	<table <%=Bean.getTableDetailParam() %>>
		<tr>
			<td><%= Bean.logisticXML.getfieldTransl("cd_lg_production", false) %></td><td><input type="text" name="cd_lg_production" size="65" value="" class="inputfield"></td>
 		    <td><%= Bean.clubXML.getfieldTransl("club", true) %>
				<%= Bean.getGoToClubLink(club.getValue("ID_CLUB")) %>
			</td>
		  	<td>
				<%=Bean.getWindowFindClub("club", club.getValue("ID_CLUB"), club.getValue("SNAME_CLUB"), "25") %>
	  		</td>
		</tr>
		<tr>
			<td><%= Bean.logisticXML.getfieldTransl("name_lg_production", true) %></td><td><input type="text" name="name_lg_production" size="65" value="" class="inputfield"></td>
			<td><%= Bean.logisticXML.getfieldTransl("cd_currency", false) %></td> <td><select name="cd_currency" id="cd_currency" class="inputfield"><%= Bean.getCurrencyOptions("", true) %></select></td>
		</tr>
		<tr>
			<td><%= Bean.logisticXML.getfieldTransl("desc_lg_production", false) %></td><td><textarea name="desc_lg_production" cols="60" rows="3" class="inputfield"></textarea></td>
			<td><%= Bean.logisticXML.getfieldTransl("cost_lg_production", false) %></td><td><input type="text" name="cost_lg_production" size="20" value="" class="inputfield"></td>
		</tr>
 		<tr>
			<td colspan="6" align="center">
				<%=Bean.getSubmitButtonAjax("../crm/logistic/partners/catalogupdate.jsp") %>
				<%=Bean.getResetButton() %>
				<% if ("add".equalsIgnoreCase(action)) { %>
					<%=Bean.getGoBackButton("../crm/logistic/partners/catalog.jsp") %>
				<% } else { %>
					<%=Bean.getGoBackButton("../crm/logistic/partners/catalogspecs.jsp?id=" + id) %>
				<% } %>	
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
			cd_lg_production			= Bean.getDecodeParam(parameters.get("cd_lg_production")),
			name_lg_production			= Bean.getDecodeParam(parameters.get("name_lg_production")),
			desc_lg_production 			= Bean.getDecodeParam(parameters.get("desc_lg_production")),
			cd_currency		 			= Bean.getDecodeParam(parameters.get("cd_currency")),
			cost_lg_production		 	= Bean.getDecodeParam(parameters.get("cost_lg_production")),
			id_club					 	= Bean.getDecodeParam(parameters.get("id_club"));

    	if (action.equalsIgnoreCase("add")) { 
    		String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_LG_CARD.add_catalog(?,?,?,?,?,?,?,?)}";

    		String[] pParam = new String [6];

    		pParam[0] = cd_lg_production;
    		pParam[1] = name_lg_production;
    		pParam[2] = desc_lg_production;
    		pParam[3] = cd_currency;
    		pParam[4] = cost_lg_production;
    		pParam[5] = id_club;
		
		 	%>
			<%= Bean.getCallResultParam("INSERT", callSQL, pParam, "../crm/logistic/partners/catalogspecs.jsp?id=" , "../crm/logistic/partners/catalog.jsp") %>
			<% 	

    	} else if (action.equalsIgnoreCase("remove")) { 
    		
    		String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_LG_CARD.delete_catalog(?,?)}";

	    	String[] pParam = new String [1];

	    	pParam[0] = id;

		 	%>
			<%= Bean.getCallResultParam("DELETE", callSQL, pParam, "../crm/logistic/partners/catalog.jsp" , "") %>
			
			<% 	

    	} else if (action.equalsIgnoreCase("edit")) { 
    		
    		String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_LG_CARD.update_catalog(?,?,?,?,?,?,?)}";

        	String[] pParam = new String [6];

        	pParam[0] = id;
        	pParam[1] = cd_lg_production;
        	pParam[2] = name_lg_production;
        	pParam[3] = desc_lg_production;
        	pParam[4] = cd_currency;
        	pParam[5] = cost_lg_production;
		
		 	%>
			<%= Bean.getCallResultParam("UPDATE", callSQL, pParam, "../crm/logistic/partners/catalogspecs.jsp?id=" + id, "") %>
			
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
