<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcCRMBean"  />

<%= Bean.getLogOutScript(request) %>

<%@page import="java.util.HashMap"%>
<%@page import="bc.objects.bcErrorTransactionObject"%>
<%@page import="bc.objects.bcClubCardOperationObject"%>
<%@page import="bc.objects.bcClubCardObject"%>
<%@page import="bc.objects.bcClubCardPurseObject"%><html>
<head>
	<%= Bean.getMetaContent() %>
	<%= Bean.getBottomFrameCSS() %>
	
</head>

<body>
<%
HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());

String pageFormName = "CARDS_CLUBCARDS";

Bean.setJspPageForTabName(pageFormName);

request.setCharacterEncoding("UTF-8");

String type			= Bean.getDecodeParamPrepare(parameters.get("type")); 
String action		= Bean.getDecodeParamPrepare(parameters.get("action")); 
String process		= Bean.getDecodeParamPrepare(parameters.get("process"));
String id			= Bean.getDecodeParamPrepare(parameters.get("id"));

if (type.equalsIgnoreCase("general")) {
	if (process.equalsIgnoreCase("yes")) {
		String
			id_card_purse 			= Bean.getDecodeParam(parameters.get("id_card_purse")),
			value_card_purse 		= Bean.getDecodeParam(parameters.get("value_card_purse"));
	
		if (action.equalsIgnoreCase("edit")) { 
		
			String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK$CARD_UI.update_card_purse(?,?,?)}";

			String[] pParam = new String [2];
					
			pParam[0] = id_card_purse;
			pParam[1] = value_card_purse;
			
		 	%>
			<%= Bean.getCallResultParam("UPDATE", callSQL, pParam, "../crm/cards/clubcardpursespecs.jsp?id="+id, "") %>
			<% 	
			
	    } else { %> 
			<%= Bean.getUnknownActionText(action) %><% 
		}
	} else {
	    %> <%= Bean.getUnknownProcessText(process) %> <%
	}
} else if (type.equalsIgnoreCase("rests")) {
	if (process.equalsIgnoreCase("yes")) {
		if (action.equalsIgnoreCase("run")) {
			String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK$CARD_UI.calc_purse_rests_initial(?,?)}";

			String[] pParam = new String [1];
							
			pParam[0] = id;
		  	     
		 	%>
			<%= Bean.getCallResultParam("UPDATE", callSQL, pParam, "../crm/cards/clubcardpursespecs.jsp?id="+id, "") %>
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
