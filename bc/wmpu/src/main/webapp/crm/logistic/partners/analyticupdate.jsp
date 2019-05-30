<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcCRMBean"  />

<%= Bean.getLogOutScript(request) %>

<%@page import="bc.objects.bcLGCardRangeObject"%>
<%@page import="java.util.HashMap"%><html>
<head>
	<%= Bean.getMetaContent() %>
	<%= Bean.getBottomFrameCSS() %>
</head>
<body>

<%
HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());

String pageFormName = "LOGISTIC_PARTNERS_ANALYTICS";

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
	if (process.equalsIgnoreCase("yes"))	{
    	
		String
    		id_jur_prs_receiver			= Bean.getDecodeParam(parameters.get("id_jur_prs_receiver")),
    		id_service_place_receiver	= Bean.getDecodeParam(parameters.get("id_service_place_receiver")),
    		desc_receiver 				= Bean.getDecodeParam(parameters.get("desc_receiver")),
    		id_jur_prs_sender 			= Bean.getDecodeParam(parameters.get("id_jur_prs_sender")),
    		id_service_place_sender 	= Bean.getDecodeParam(parameters.get("id_service_place_sender")),
    		desc_sender 				= Bean.getDecodeParam(parameters.get("desc_sender")),
    		action_date 				= Bean.getDecodeParam(parameters.get("action_date")),
    		object_count 				= Bean.getDecodeParam(parameters.get("object_count")),
    		operation_desc 				= Bean.getDecodeParam(parameters.get("operation_desc"));
    		
		String id_club = Bean.getCurrentClubID();

    	if (action.equalsIgnoreCase("runall")) { 
    		String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_LG_CARD.calc_analytic_club(?,?)}";

    		String[] pParam = new String [1];

    		pParam[0] = id_club;
		
		 	%>
			<%= Bean.getCallResultParam("UPDATE", callSQL, pParam, "../crm/logistic/partners/analytics.jsp" , "") %>
			<% 	

    	} else if (action.equalsIgnoreCase("run")) { 
    		
    		String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_LG_CARD.calc_analytic_jur_prs(?,?,?)}";

    	    String[] pParam = new String [2];

    	    pParam[0] = id_club;
    	    pParam[1] = id;
		
		 	%>
			<%= Bean.getCallResultParam("UPDATE", callSQL, pParam, "../crm/logistic/partners/analyticspecs.jsp?id=" + id, "") %>
			
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
