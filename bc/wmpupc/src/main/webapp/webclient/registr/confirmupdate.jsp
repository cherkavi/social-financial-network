<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcWebClientBean"  />

<%= Bean.getLogOutScript(request) %>

<%@page import="java.util.HashMap"%>
<%@page import="bc.objects.bcPostingEditObject"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Set"%>
<%@page import="java.util.Iterator"%><html>
<head>
	<%= Bean.getMetaContent() %>

</head>

<body>
<%
HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());

String pageFormName = "CARD_CHANGE";

Bean.setJspPageForTabName(pageFormName);

request.setCharacterEncoding("UTF-8");
String type		= Bean.getDecodeParam(parameters.get("type")); 
String id		= Bean.getDecodeParam(parameters.get("id")); 
String action	= Bean.getDecodeParam(parameters.get("action")); 
String process	= Bean.getDecodeParam(parameters.get("process"));

if (id==null || ("".equalsIgnoreCase(id))) id="";
if (type==null || ("".equalsIgnoreCase(type))) type="";
if (action==null || ("".equalsIgnoreCase(action))) action="";
if (process==null || ("".equalsIgnoreCase(process))) process="";

if (type.equalsIgnoreCase("card")) {

	if (process.equalsIgnoreCase("yes")) {
		String
			id_user						= Bean.loginUser.getValue("ID_USER"),
			card_serial_number			= Bean.getDecodeParam(parameters.get("id")),
			id_isssuer 					= Bean.getDecodeParam(parameters.get("iss")),
			id_payment_system			= Bean.getDecodeParam(parameters.get("paysys"));

		if (action.equalsIgnoreCase("get_base")) {
			
			String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK$UI_CARD.get_base_card(?,?,?,?)}";

			String[] pParam = new String [3];
			
			pParam[0] = card_serial_number;
			pParam[1] = id_isssuer;
			pParam[2] = id_payment_system;

    	 	%>
    		<%= Bean.getCallResultParam("UPDATE", callSQL, pParam, "/card/card_param.jsp", "") %>
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
