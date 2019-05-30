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

String pageFormName = "FINANCE_AUTORECONCIL";

Bean.setJspPageForTabName(pageFormName);

request.setCharacterEncoding("UTF-8");

String action	= Bean.getDecodeParam(parameters.get("action")); 
String process	= Bean.getDecodeParam(parameters.get("process"));

if (action==null || ("".equalsIgnoreCase(action))) action="empty";
if (process==null || ("".equalsIgnoreCase(process))) process="empty";

if (process.equalsIgnoreCase("no")){
	if (action.equalsIgnoreCase("run")) {

		bcClubShortObject club = new bcClubShortObject(Bean.getCurrentClubID());
		
		%>
			<%= Bean.getOperationTitle(
					Bean.kvitovkaXML.getfieldTransl("LAB_RUN_AUTORECONCIL", false),
					"Y",
					"N") 
			%>
		<form action="../crm/finance/autoreconcilupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return validateForm(formData)">
		    <input type="hidden" name="action" value="RUN">
		    <input type="hidden" name="process" value="yes">
		<table <%=Bean.getTableDetailParam() %>>
			<tr>
				<td><%= Bean.clubXML.getfieldTransl("club", false) %>
					<%= Bean.getGoToClubLink(club.getValue("ID_CLUB")) %>
			  	</td>
			  	<td>
					<%=Bean.getWindowFindClub("club", club.getValue("ID_CLUB"), club.getValue("SNAME_CLUB"), "25") %>
			  	</td>
			</tr>
		    <tr>
		       <td align="left"> 
		          <font size=2><%= Bean.postingXML.getfieldTransl("debug", false) %></font>
		       </td>
		       <td align="left"><select name="debug" class="inputfield"><%= Bean.getMeaningFromLookupNameOptions("YES_NO", Bean.getUIUserParam("BANK_STATEMENT_LOAD_DEBUG"), false) %></select></td>
		    </tr>
			<tr>
				<td colspan="6" align="center">
					<%=Bean.getSubmitButtonAjax("../crm/finance/autoreconcilupdate.jsp", "submit", "updateForm") %>
					<%=Bean.getResetButton() %>
					<%=Bean.getGoBackButton("../crm/finance/autoreconcil.jsp") %>
				</td>
			</tr>
		</table>
		</form>

	<% 
	} else { %> 
   		<%= Bean.getUnknownActionText(action) %><% 
   	}
} else if (process.equalsIgnoreCase("yes")){

	if (action.equalsIgnoreCase("RUN")) { 

		String
			id_club 		= Bean.getDecodeParam(parameters.get("id_club")),
			debug	 		= Bean.getDecodeParam(parameters.get("debug"));
		
   		String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_AUTORECONCIL.autoreconcilation(?,?,?,?)}";

		String[] pParam = new String [2];

		pParam[0] = id_club;
		pParam[1] = debug;

		%>
		<%= Bean.getCallResultParam("RUN", callSQL, pParam, "../crm/finance/autoreconcilspecs.jsp?id=", "../crm/finance/autoreconcil.jsp", "../crm/finance/autoreconcil.jsp") %>
		<% 	

   	} else { %> 
   		<%= Bean.getUnknownActionText(action) %><% 
   	}
} else {
    %> <%= Bean.getUnknownProcessText(process) %> <%
}

%>


</body>
</html>
