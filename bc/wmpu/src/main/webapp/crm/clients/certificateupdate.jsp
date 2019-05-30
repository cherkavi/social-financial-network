<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcCRMBean"  />

<%@page import="bc.objects.bcNatPrsObject"%>

<%= Bean.getLogOutScript(request) %>

<%
HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());

String pageFormName = "CLIENTS_CERTIFICATE";

Bean.setJspPageForTabName(pageFormName);

request.setCharacterEncoding("UTF-8");

String type			= Bean.getDecodeParam(parameters.get("type"));
String id_cert		= Bean.getDecodeParam(parameters.get("id_cert")); 
String id_term		= Bean.getDecodeParam(parameters.get("id_term"));
String action		= Bean.getDecodeParam(parameters.get("action")); 
String process		= Bean.getDecodeParam(parameters.get("process"));
String filtr_type	= Bean.getDecodeParam(parameters.get("filtr_type"));

if (type==null || ("".equalsIgnoreCase(type))) type="empty";
if (id_cert==null || ("".equalsIgnoreCase(id_cert))) id_cert="empty";
if (id_term==null || ("".equalsIgnoreCase(id_term))) id_term="";
if (action==null || ("".equalsIgnoreCase(action))) action="empty";
if (process==null || ("".equalsIgnoreCase(process))) process="empty";
if (filtr_type==null || ("".equalsIgnoreCase(filtr_type))) filtr_type="";

String callSQL = "";
%>

<%@page import="bc.objects.bcTerminalCertificateObject"%>
<%@page import="bc.AppConst"%>
<%@page import="java.util.HashMap"%><html>
<head>
	<%= Bean.getMetaContent() %>
	<%= Bean.getBottomFrameCSS() %>

</head>


<body>
<%
if (type.equalsIgnoreCase("general")) {
	if (process.equalsIgnoreCase("yes")) {
    
		String    
    		begin_action_date 			= Bean.getDecodeParam(parameters.get("begin_action_date")),
    		end_action_date 			= Bean.getDecodeParam(parameters.get("end_action_date")), 
    		text_certificate			= Bean.getDecodeParam(parameters.get("text_certificate")); 
    
	    if (action.equalsIgnoreCase("remove")) { 
	    	
    		callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK$TERM_CERTIFICATE_UI.delete_certificate(?,?,?)}";

    	 	String[] pParam = new String [2];
    	 		 	    	      				
    	 	pParam[0] = id_cert;
    		pParam[1] = filtr_type;

   	 		%>
   			<%= Bean.getCallResultParam("DELETE", callSQL, pParam, "../crm/clients/certificate.jsp?id_term=" + id_term + "&id_profile=" +filtr_type, "") %>
   			<% 	

	    } else if (action.equalsIgnoreCase("remove_from_term")) { 
   		    	
    		callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK$TERM_CERTIFICATE_UI.delete_certificate(?,?,?)}";

    	 	String[] pParam = new String [2];
   	    	 		 	    	      				
    	 	pParam[0] = id_cert;
    		pParam[1] = filtr_type;

   	 		%>
   			<%= Bean.getCallResultParam("DELETE", callSQL, pParam, "../crm/clients/certificatespecs.jsp?id_term=" + id_term + "&id_profile=T", "") %>
   			<% 	

	    } else if (action.equalsIgnoreCase("edit")) { 	
		
			callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK$TERM_CERTIFICATE_UI.update_certificate(" + 
				"?,?,?,?,?,?,?,?)}";

    	 	String[] pParam = new String [7];
	    	 		 	    	      				
    	 	pParam[0] = id_cert;
    		pParam[1] = id_term;
    		pParam[2] = text_certificate;
    		pParam[3] = begin_action_date;
    		pParam[4] = end_action_date;
    		pParam[5] = filtr_type;
    		pParam[6] = Bean.getDateFormat();

   	 		%>
   			<%= Bean.getCallResultParam("UPDATE", callSQL, pParam, "../crm/clients/certificatespecs.jsp?id_term=" + id_term + "&id_profile=" +filtr_type + "&id_cert=" + id_cert, "") %>
   			<% 	

	    }  else { %> 
			<%= Bean.getUnknownActionText(action) %><% 
		}
	} else {%> 
		<%= Bean.getUnknownProcessText(process) %> <% 
	}
	
} else if (type.equalsIgnoreCase("apply")) { 
	
	callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK$TERM_CERTIFICATE_UI.apply_certificate(?,?)}";

 	String[] pParam = new String [1];
	 		 	    	      				
 	pParam[0] = id_cert;

	%>
	<%= Bean.getCallResultParam("UPDATE", callSQL, pParam, "../crm/clients/certificatespecs.jsp?id_profile=C&id_cert=" + id_cert + "&id_term=" + id_term, "../crm/clients/certificatespecs.jsp?id_profile=" + filtr_type + "&id_cert=" + id_cert + "&id_term=" + id_term) %>
	<% 	

} else if (type.equalsIgnoreCase("applyall")) { 
	
	callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK$TERM_CERTIFICATE_UI.apply_all_certificates(?)}";

	String[] pParam = new String [0];
	%>
	<%= Bean.getCallResultParam("UPDATE", callSQL, pParam, "../crm/clients/certificate.jsp?id_profile=C", "") %>
	<% 	

} else if (type.equalsIgnoreCase("set_to_term")) { 
	
	callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK$TERM_CERTIFICATE_UI.make_current(?,?,?)}";

	String[] pParam = new String [2];
		 		 	    	      				
	pParam[0] = id_cert;
	pParam[1] = id_term;

	%>
	<%= Bean.getCallResultParam("UPDATE", callSQL, pParam, "../crm/clients/certificatespecs.jsp?id_term=" + id_term + "&id_profile=T&tab=1&id_cert=" + id_cert, "") %>
	<% 	

} else if (type.equalsIgnoreCase("revoke_from_term")) { 
	
	callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK$TERM_CERTIFICATE_UI.revoke_current(?,?)}";

	String[] pParam = new String [1];
			 		 	    	      				
	pParam[0] = id_term;

	%>
	<%= Bean.getCallResultParam("UPDATE", callSQL, pParam, "../crm/clients/certificatespecs.jsp?id_term=" + id_term + "&id_profile=T&id_cert=" + id_cert, "") %>
	<% 	

} else {
	%><%= Bean.getUnknownTypeText(type) %><%
}

%>

</body>
</html>
