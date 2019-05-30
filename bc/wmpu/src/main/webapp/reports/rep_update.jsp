<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcCRMBean"  />

<%= Bean.getLogOutScript(request) %>

<%@page import="java.util.HashMap"%>
<%@page import="java.util.Enumeration"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Set"%>
<%@page import="java.util.Iterator"%>
<%@page import="bc.objects.bcClubShortObject"%><html>
<head>
	<%= Bean.getMetaContent() %>
	<%= Bean.getBottomFrameCSS() %>

</head>

<body>
<%
HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());

String pageFormName = "REPORTS_SELECTED";

Bean.setJspPageForTabName(pageFormName);

request.setCharacterEncoding("UTF-8");
String id		= Bean.getDecodeParam(parameters.get("id")); 
String type	= Bean.getDecodeParam(parameters.get("type"));
String action	= Bean.getDecodeParam(parameters.get("action")); 
String process	= Bean.getDecodeParam(parameters.get("process"));

if (id==null || ("".equalsIgnoreCase(id))) id="empty";
if (type==null || ("".equalsIgnoreCase(type))) type="empty";
if (action==null || ("".equalsIgnoreCase(action))) action="empty";
if (process==null || ("".equalsIgnoreCase(process))) process="empty";

ArrayList<String> id_value=new ArrayList<String>();

String callSQL = "";
Object current_parameter;
	Set<String> keySet = parameters.keySet();
	Iterator<String> keySetIterator = keySet.iterator();
	String key = "";
	while(keySetIterator.hasNext()) {
		try{
			key = (String)keySetIterator.next();
			if(key.contains("chb")){
				id_value.add(key.substring(3));
			}
			
		}
		catch(Exception ex){
			Bean.writeException(
						"../reports/rep_update.jsp",
						"",
						process,
						action,
						Bean.commonXML.getfieldTransl("h_get_param_value_error", false)+key+": " + ex.toString());
		}
		 
	}
	
String id_user = Bean.loginUser.getValue("ID_USER");



if (type.equalsIgnoreCase("to_selected")) {
	if (process.equalsIgnoreCase("yes")) {    
	    String[] results = new String[2];
	    String resultInt = "";
	    String resultFull = "0";
	    String resultMessage = "";
	    String resultMessageFull = "";
	    
	    String[] pParam = new String [2];
	
	    if (action.equalsIgnoreCase("set")) { 
	    
	    	String id_menu_element = Bean.getDecodeParam(parameters.get("id_menu_element"));
	    	String to_jsp_page = "";
	    	if ("710".equalsIgnoreCase(id_menu_element)) {
	    		to_jsp_page = "../reports/rep_system.jsp";
	    	} else if ("720".equalsIgnoreCase(id_menu_element)) {
	    		to_jsp_page = "../reports/rep_accounting.jsp";
	    	} else if ("730".equalsIgnoreCase(id_menu_element)) {
	    		to_jsp_page = "../reports/rep_marketing.jsp";
	    	} else if ("740".equalsIgnoreCase(id_menu_element)) {
	    		to_jsp_page = "../reports/rep_po_partners.jsp";
	    	} else if ("750".equalsIgnoreCase(id_menu_element)) {
	    		to_jsp_page = "../reports/rep_po_clients.jsp";
	    	}
	        %>
	
			<%= Bean.form_messageXML.getfieldTransl("processing_update", false) %>
			<%
			if (!(id_value == null)) {
			 for(int counter=0;counter<id_value.size();counter++){
		    	callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK$REPORT_UI.to_selected(?,?,?)}";
	        		
	        	pParam[0] = id_user;
			    pParam[1] = id_value.get(counter);
				
				results = Bean.myCallFunctionParam(callSQL, pParam, 2);
				resultInt = results[0];
				resultMessage = results[1];
				
				%>
				<%= Bean.showCallSQL(callSQL) %>
				<%
				
				if (!("0".equalsIgnoreCase(resultInt))) {
					resultFull = resultInt;
					resultMessageFull = resultMessageFull + "; " +resultMessage;
				}
			 }
			}
			
			%>
	  	    <%=Bean.showCallResult(
	   	    		callSQL, 
	   	    		resultFull, 
	   	    		resultMessageFull, 
	   	    		to_jsp_page, 
	   	    		to_jsp_page, 
	   	    		Bean.form_messageXML.getfieldTransl("save_error", false)) %>
	   		<% 
	
	    } else if (action.equalsIgnoreCase("delete")) { %>
		
			<%= Bean.form_messageXML.getfieldTransl("processing_update", false) %>
			<%
			if (!(id_value == null)) {
			 for(int counter=0;counter<id_value.size();counter++){
		    	callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK$REPORT_UI.from_selected(?,?,?)}";
	        		
	        	pParam[0] = id_user;
			    pParam[1] = id_value.get(counter);
				
				results = Bean.myCallFunctionParam(callSQL, pParam, 2);
				resultInt = results[0];
				resultMessage = results[1];
				
				%>
				<%= Bean.showCallSQL(callSQL) %>
				<%
				
				if (!("0".equalsIgnoreCase(resultInt))) {
					resultFull = resultInt;
					resultMessageFull = resultMessageFull + "; " +resultMessage;
				}
			 }
			}
			
			%>
	  	    <%=Bean.showCallResult(
	   	    		callSQL, 
	   	    		resultFull, 
	   	    		resultMessageFull, 
	   	    		"../reports/rep_selected.jsp", 
	   	    		"../reports/rep_selected.jsp", 
	   	    		Bean.form_messageXML.getfieldTransl("save_error", false)) %>
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
