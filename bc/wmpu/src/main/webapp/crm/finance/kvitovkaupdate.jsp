<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcCRMBean"  />

<%= Bean.getLogOutScript(request) %>

<%@page import="java.util.HashMap"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.Set"%>
<%@page import="java.util.Iterator"%><html>
<head>
	<%= Bean.getMetaContent() %>
	<%= Bean.getBottomFrameCSS() %>
</head>

<body>
<%
HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());

String pageFormName = "FINANCE_KVITOVKA";

Bean.setJspPageForTabName(pageFormName);

request.setCharacterEncoding("UTF-8");

String id		= Bean.getDecodeParam(parameters.get("id_clearing_line")); 
String action	= Bean.getDecodeParam(parameters.get("action")); 
String process	= Bean.getDecodeParam(parameters.get("process"));

if (id==null || ("".equalsIgnoreCase(id))) id="empty";
if (action==null || ("".equalsIgnoreCase(action))) action="empty";
if (process==null || ("".equalsIgnoreCase(process))) process="empty";

if (process.equalsIgnoreCase("yes")){
    String
    	l_bank_statement	= Bean.getDecodeParam(parameters.get("bank_statement")),
    	l_reconciled_sum	= Bean.getDecodeParam(parameters.get("reconciled_sum"));
    	if ((l_bank_statement==null)||"".equalsIgnoreCase(l_bank_statement)) {
    		l_bank_statement = "NULL";
    	}

	if (action.equalsIgnoreCase("reconcile")) { %>
		
		<%= Bean.form_messageXML.getfieldTransl("processing_update", false) %>
   	   	<% 
    
   	   	String callSQL = "";
   	   	
   	   	String[] results = new String[2];

   		String id_bank_statement_line = Bean.getDecodeParam(parameters.get("id_bank_statement_line"));
 		
   		ArrayList<String> checkbox_value=new ArrayList<String>();
		Map<String,String> sum_value=new HashMap<String,String>();

    	Set keySet = parameters.keySet();
		Iterator keySetIterator = keySet.iterator();
		String key = "";
    	while(keySetIterator.hasNext()) {
   			try{
   				key = (String)keySetIterator.next();
   				if(key.contains("id_")){
   					checkbox_value.add(key.substring(3));
   				}
   				if(key.contains("sum_")){
   					sum_value.put(key.substring(4), Bean.getDecodeParam(parameters.get(key)));
   				}
   			}
   			catch(Exception ex){
   				Bean.writeException(
   						"../crm/finance/kvitovkaupdate.jsp",
   						"",
   						process,
   						action,
   						Bean.commonXML.getfieldTransl("h_get_param_value_error", false) + key+": " + ex.toString());
   			}
   		}
   		
   	    String resultInt = "";
   	    String resultFull = "0";
   	    String resultMessage = "";
   	    String resultMessageFull = "";
   	    
   	 	String[] pParam = new String [4];
   	    
	   	if (checkbox_value.size()>0) {
	 		 for(int counter=0;counter<checkbox_value.size();counter++){
	 			 if (sum_value.containsKey(checkbox_value.get(counter))) {
	 				callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_KVITOVKA.set_reconcile_status(?,?,?,?,?)}";
	 					
	 				pParam[0] = checkbox_value.get(counter);
	 				pParam[1] = id_bank_statement_line;
	 				pParam[2] = sum_value.get(checkbox_value.get(counter));
	 				pParam[3] = "RECONCILE";
	 					
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
		}
	   	 
		%>
    	<%=Bean.showCallResult(callSQL, 
    		resultFull, 
    		resultMessage, 
    		"../crm/club/kvitovka.jsp?id_bank_statement_line=" + id_bank_statement_line, 
    		"../crm/club/kvitovka.jsp?id_bank_statement_line=" + id_bank_statement_line, 
    		Bean.form_messageXML.getfieldTransl("save_error", false)) %>
		<% 
   	    
   	} else if (action.equalsIgnoreCase("unreconcile")) { 

   		String id_bank_statement_line = Bean.getDecodeParam(parameters.get("id_bank_statement_line"));
   		
   		String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_KVITOVKA.set_reconcile_status(?,?,?,?,?)}";

		String[] pParam = new String [4];

		pParam[0] = "";
		pParam[1] = id_bank_statement_line;
		pParam[2] = "";
		pParam[3] = "UNRECONCILE";

		%>
		<%= Bean.getCallResultParam("UPDATE", callSQL, pParam, "../crm/finance/kvitovka.jsp?id_clearing_line=" + id, "") %>
		<% 	

   	} else if (action.equalsIgnoreCase("autoreconcilation")) { 
	  
   		String 
   			trace_level 	= Bean.getDecodeParam(parameters.get("trace_level")),
	   		debug 		= Bean.getDecodeParam(parameters.get("debug"));

   		String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_KVITOVKA.autoreconcilation(?,?,?)}";

		String[] pParam = new String [2];

		pParam[0] = "";
		pParam[1] = debug;

		%>
		<%= Bean.getCallResultParam("UPDATE", callSQL, pParam, "../crm/finance/kvitovkaspecs.jsp?id=", "") %>
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
