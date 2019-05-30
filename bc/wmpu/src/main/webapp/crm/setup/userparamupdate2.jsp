<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcCRMBean"  />

<%= Bean.getLogOutScript(request) %>

<%@page import="java.util.HashMap"%><html>
<head>
	<%= Bean.getMetaContent() %>
	<%= Bean.getBottomFrameCSS() %>
</head>

<body>
<%
HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());


request.setCharacterEncoding("UTF-8");

String id		= Bean.getDecodeParam(parameters.get("id")); 
String action	= Bean.getDecodeParam(parameters.get("action")); 
String process	= Bean.getDecodeParam(parameters.get("process"));

if (id==null || ("".equalsIgnoreCase(id))) id="empty";
if (action==null || ("".equalsIgnoreCase(action))) action="empty";
if (process==null || ("".equalsIgnoreCase(process))) process="empty";

if (process.equalsIgnoreCase("yes"))
	{
    String  
    l_paramCount	= Bean.getDecodeParam(parameters.get("paramcount"));
    
    String update_sql = "";
    String nameParam = "";
    String valueParam = "";
    String fullResult = "0";
	
    if (action.equalsIgnoreCase("edit")) { %>

		<%= Bean.form_messageXML.getfieldTransl("processing_update", false) %>
		<% 
		 
	    int i;
		  
		String[] results = new String[2];
		String callSQL = "";
		String resultInt = "";
		String resultMessage = "";
		String myLang = "UA";

		String[] pParam = new String [5];
		  
		for (i=1;i<=Integer.parseInt(l_paramCount);i++) {
			nameParam	= Bean.getDecodeParam(parameters.get("nameparam"+i));
			valueParam	= Bean.getDecodeParam(parameters.get("valueparam"+i));
			
			callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK$USER_UI.change_user_param(?,?,?,?,?,?)}";

			pParam[0] = id;
			pParam[1] = nameParam;
			pParam[2] = valueParam;
			pParam[3] = "Y";
			pParam[4] = "N";
				
			%>
			<%= Bean.showCallSQL(callSQL) %>
			<%

			results = Bean.myCallFunctionParam(callSQL, pParam, 2);
			resultInt = results[0];
			resultMessage = results[1];
			if ("UIL".equalsIgnoreCase(nameParam)) {
				myLang = valueParam;
			}
	

		if (!("0".equalsIgnoreCase(resultInt))) { 
				fullResult = fullResult + ", " + resultMessage;
			}
		}
		%>
        <script language="JavaScript">
			indx_location = '../crm/main.jsp?do=exit&lang=<%=myLang%>';
			top.location.href = indx_location;
		 </script>
   		<% 

    } else if (action.equalsIgnoreCase("update_password")) { 
    	
	    String[] results = new String[2];

	    String
    		password			= Bean.getDecodeParam(parameters.get("password")),
    		confirm_password 	= Bean.getDecodeParam(parameters.get("confirm_password"));
			     
		String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK$USER_UI.change_password(?,?,?)}";

		String[] pParam = new String [2];

		pParam[0] = password;
		pParam[1] = confirm_password;
			
	 	results = Bean.myCallFunctionParam(callSQL, pParam, 2);
	 	String resultInt = results[0];
	 	String resultMessage = results[1];
	 		
 		if ("0".equalsIgnoreCase(resultInt)) { %>
	   		<script language="JavaScript">
				indx_location = '../crm/main.jsp?do=exit';
				top.location.href = indx_location;
		 	</script>
		 <% } else { %>
			<%=Bean.showCallResult(
   	    		callSQL, 
   	    		resultInt, 
   	    		resultMessage, 
   	    		"/main2.jsp?do=exit", 
   	    		"../crm/setup/userparam.jsp", 
   	    		Bean.form_messageXML.getfieldTransl("save_error", false)) %>
		<% }%>
   		<%

    } else { %> 
    	<%= Bean.getUnknownActionText(action) %><% 
    }
} else {%> 
	<%= Bean.getUnknownProcessText(process) %> <%
}

%>


</body>
</html>
