<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcCRMBean"  />
<%@page import="bc.objects.bcUserObject"%>
<%@page import="java.util.HashMap"%><html>

<%= Bean.getLogOutScript(request) %>

<head>
	<%= Bean.getMetaContent() %>
	<%= Bean.getBottomFrameCSS() %>
</head>

<body>
<%
HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());

String pageFormName = "SECURITY_USERS";

Bean.setJspPageForTabName(pageFormName);

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
    	ID_USER	= Bean.getDecodeParam(parameters.get("user_id"));
    
    String fullResult = "0";
    
    if (action.equalsIgnoreCase("edit")) { %>  

		<%= Bean.form_messageXML.getfieldTransl("processing_update", false) %>
		<% 
  		request.setCharacterEncoding("UTF-8");
  		String role_cnt = Bean.getDecodeParam(parameters.get("rowCount"));
  	
  	  	String RoleN="";
	  	String tprvCheck="";
	  	String DateRoleprvCheck="";
	  	String tCheck="";
	  
	  	String callSQL = "";
	  	String[] results = new String[2];
	  	String resultInt = "";
	  	String resultMessage = "";
		
	 	String[] pParam = new String [2];
	  
     	for (int i=1; i<=Integer.parseInt(role_cnt); i++) {
      		tCheck 			= Bean.getDecodeParam(parameters.get("chk_r_"+i))+ "";
      		tprvCheck			= Bean.getDecodeParam(parameters.get("prvchk_r_"+i));
      		DateRoleprvCheck	= Bean.getDecodeParam(parameters.get("dchk_r_"+i));
      		RoleN				= Bean.getDecodeParam(parameters.get("r_"+i));
			%> 
    		 <br>
       		<% 
	  
			if ((tCheck==null || "".equalsIgnoreCase(tCheck) || "null".equalsIgnoreCase(tCheck)) && (!"0".equalsIgnoreCase(tprvCheck)) ) {
  				// удаляем те которые стали нулл а были не нулл
  				callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK$USER_UI.delete_user_role(?,?,?)}";
  	  			
  				pParam[0] = ID_USER;
  				pParam[1] = RoleN;
  				
  				results = Bean.myCallFunctionParam(callSQL, pParam, 2);
  				resultInt = results[0];
  				resultMessage = results[1];
		 	
 	 			if (!("0".equalsIgnoreCase(resultInt))){
		 			fullResult = fullResult + ", " + resultMessage ;
	 			}
  
  
  			} else if (!(tCheck==null || "".equalsIgnoreCase(tCheck) || "null".equalsIgnoreCase(tCheck)) && "0".equalsIgnoreCase(tprvCheck)) {
	  			// вставляем те которые были null а стали не нулл
	  			callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK$USER_UI.add_user_role(?,?,?)}";
  	  	  			
  				pParam[0] = ID_USER;
  				pParam[1] = RoleN;
  	  				
  				results = Bean.myCallFunctionParam(callSQL, pParam, 2);
  				resultInt = results[0];
  				resultMessage = results[1];
		 	
 	 			if (!("0".equalsIgnoreCase(resultInt))){
 	 				fullResult = fullResult + ", " + resultMessage ;
	 			}
  			}
 		}
     	
     	callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK$USER_UI.sync_current_role(?,?,?)}";
		
		String[] pParam2 = new String [2];

		pParam2[0] = ID_USER;
		pParam2[0] = "CRM";
			
		results = Bean.myCallFunctionParam(callSQL, pParam2, 2);
		resultInt = results[0];
		resultMessage = results[1];
	
		if (!("0".equalsIgnoreCase(resultInt))){
			fullResult = fullResult + ", " + resultMessage ;
		}
 		%>

  	    <%=Bean.showCallResult(
   	    		callSQL, 
   	    		fullResult, 
   	    		fullResult, 
   	    		"../crm/security/userspecs.jsp?id=" + ID_USER, 
   	    		"../crm/security/userspecs.jsp?id=" + ID_USER, 
   	    		Bean.form_messageXML.getfieldTransl("save_error", false)) %>
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
