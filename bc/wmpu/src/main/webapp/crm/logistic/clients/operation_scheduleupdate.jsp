<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcCRMBean"  />

<%= Bean.getLogOutScript(request) %>

<%@page import="bc.objects.bcLGCardRangeObject"%>
<%@page import="java.util.HashMap"%>
<%@page import="bc.objects.bcClubShortObject"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.Set"%>
<%@page import="java.util.Iterator"%><html>
	<%= Bean.getMetaContent() %>
	<%= Bean.getBottomFrameCSS() %>
<%
HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());

String pageFormName = "LOGISTIC_CLIENTS_OPERATION_SCHEDULE";

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
			date_card_given					= Bean.getDecodeParam(parameters.get("date_card_given")),
			notes							= Bean.getDecodeParam(parameters.get("notes")),
			id_club							= Bean.getDecodeParam(parameters.get("id_club"));

    	if (action.equalsIgnoreCase("edit")) { 
    		String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_LG_CARD.update_ca_given_schedule(?,?,?)}";

			String[] pParam = new String [2];

			pParam[0] = id;
			pParam[1] = notes;
		
		 	%>
			<%= Bean.getCallResultParam("UPDATE", callSQL, pParam, "../crm/logistic/clients/operation_schedulespecs.jsp?id=" + id, "../crm/logistic/clients/operation_schedule.jsp") %>
			<% 	

    	} else { %> 
    		<%= Bean.getUnknownActionText(action) %><% 
    	}
	} else {
    	%> <%= Bean.getUnknownProcessText(process) %> <%
	}

} else if (type.equalsIgnoreCase("set_schedule")) {
	if (process.equalsIgnoreCase("yes")) {
		if (action.equalsIgnoreCase("set")) {%> 
    
			<%= Bean.form_messageXML.getfieldTransl("processing_update", false) %>
		   	<% 
	
		String[] results = new String[2];
		   	
		ArrayList<String> id_value = new ArrayList<String>();
		//Map<String,String> sp_value = new HashMap<String,String>();
		//Map<String,String> state_value = new HashMap<String,String>();
		Map<String,String> begin_value = new HashMap<String,String>();
		Map<String,String> end_value = new HashMap<String,String>();
		//Map<String,String> cards_value = new HashMap<String,String>();
		Map<String,String> notes_value = new HashMap<String,String>();
	
		String callSQL = "";
	
		Set<String> keySet = parameters.keySet();
		Iterator<String> keySetIterator = keySet.iterator();
		String key = "";
		while(keySetIterator.hasNext()) {
				try{
					key = (String)keySetIterator.next();
					//System.out.println(key + " = '" + parameters.get(key) + "'");
					if(key.contains("id_")){
						id_value.add(key.substring(3));
					}
					//if(key.contains("sp_")){
					//	sp_value.put(key.substring(3), Bean.getDecodeParam(parameters.get(key)));
					//}
					//if(key.contains("state_")){
					//	state_value.put(key.substring(6), Bean.getDecodeParam(parameters.get(key)));
					//}
					if(key.contains("begin_")){
						begin_value.put(key.substring(6), Bean.getDecodeParam(parameters.get(key)));
					}
					if(key.contains("end_")){
						end_value.put(key.substring(4), Bean.getDecodeParam(parameters.get(key)));
					}
					//if(key.contains("cards_")){
					//	cards_value.put(key.substring(6), Bean.getDecodeParam(parameters.get(key)));
					//}
					if(key.contains("notes_")){
						notes_value.put(key.substring(6), Bean.getDecodeParam(parameters.get(key)));
					}
				}
				catch(Exception ex){
					Bean.writeException(
							"../crm/logistic/clients/operation_scheduleupdate.jsp",
							type,
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
		
		    
		if (id_value.size()>0) {
	 		 for(int counter=0;counter<id_value.size();counter++){
	 			//String sp = sp_value.get(id_value.get(counter));
	 			//String state = state_value.get(id_value.get(counter));
	 			String begin = begin_value.get(id_value.get(counter));
	 			String end = end_value.get(id_value.get(counter));
	 			//String cards = cards_value.get(id_value.get(counter));
	 			String notes = notes_value.get(id_value.get(counter));
	 			
	 			callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_LG_CARD.update_ca_given_schedule_line(?,?,?,?,?)}";
		        		
		        pParam[0] = id_value.get(counter);
		    	pParam[1] = begin;
		    	pParam[2] = end;
		    	pParam[3] = notes;
			
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
	    	<%=Bean.showCallResult(callSQL, 
	    		resultFull, 
	    		resultMessage, 
	    		"../crm/logistic/clients/operation_schedulespecs.jsp?id=" + id, 
	    		"../crm/logistic/clients/operation_schedulespecs.jsp?id=" + id, 
	    		Bean.form_messageXML.getfieldTransl("save_error", false)) %>
			<% 
		
		} else { %>
			<%= Bean.getUnknownActionText(action) %><% 
		} 
	} else { %>
		<%= Bean.getUnknownProcessText(process) %><% 
	}
} else {
	%> <%= Bean.getUnknownTypeText(type) %> <%
}

%>


</html>
