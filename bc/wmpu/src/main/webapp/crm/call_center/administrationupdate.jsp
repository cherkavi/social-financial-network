<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcCRMBean"  />

<%= Bean.getLogOutScript(request) %>

<%@page import="java.util.ArrayList"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Set"%>
<%@page import="java.util.Iterator"%><html>
<head>
	<%= Bean.getMetaContent() %>
	<%= Bean.getBottomFrameCSS() %>

</head>
 
<body>
<%
HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());

String pageFormName = "CALL_CENTER_ADMINISTRATION";
Bean.setJspPageForTabName(pageFormName);

request.setCharacterEncoding("UTF-8");

String type		= Bean.getDecodeParam(parameters.get("type"));
String id		= Bean.getDecodeParam(parameters.get("id"));
String action	= Bean.getDecodeParam(parameters.get("action")); 
String process	= Bean.getDecodeParam(parameters.get("process"));

if (id==null || ("".equalsIgnoreCase(id))) id="empty";
if (action==null || ("".equalsIgnoreCase(action))) action="empty";
if (process==null || ("".equalsIgnoreCase(process))) process="empty";
if (type==null || ("".equalsIgnoreCase(type))) type="empty";

if (type.equalsIgnoreCase("general")) {
	if (process.equalsIgnoreCase("no"))
	/* вибираємо тип дії (добавити, видалити...)*/
	{
	   /*  --- Добавити запис --- */
    	if (action.equalsIgnoreCase("add") || action.equalsIgnoreCase("add2")) {%>

 
		<%= Bean.getOperationTitle(
				Bean.call_centerXML.getfieldTransl("h_add_user", false),
				"Y",
				"N") 
		%>
	<script>
	// Скрипты для пользователей Call-центра
	 var prefix="p_";
	 var suffixCounter=0;
	 function move(source, destination,method){
	  for(var counter=(source.options.length-1);counter>=0;counter--){
	   if(source.options[counter].selected){
	    var element=source.options[counter];
	    source.removeChild(element);
	    destination.appendChild(element);

	    method(element);
	   }
	  }
	 }

	 function removeElementFromForm(element){
		  var formDestination=document.getElementById("p_span");
		  var elem = document.getElementById(prefix + element.value);
		  formDestination.removeChild(document.getElementById(elem.name));
	 }

	 function addElementToForm(element){
	  var elementText=element.text;
	  var hiddenElement=document.createElement("input");
	  hiddenElement.name=prefix+element.getAttribute("value");//prefix+suffixCounter;
	  hiddenElement.id=prefix+element.getAttribute("value");//prefix+suffixCounter;
	  hiddenElement.type="hidden";
	  hiddenElement.value=element.getAttribute("value");
	  formDestination=document.getElementById("p_span");
	  formDestination.appendChild(hiddenElement);
	 }


	 function fromSource(){
	  var source=document.getElementById("source");
	  var destination=document.getElementById("destination");
	  move(source,destination,addElementToForm);
	 }

	 function fromDestination(){
	  var source=document.getElementById("destination");
	  var destination=document.getElementById("source");
	  move(source,destination,removeElementFromForm);
	 }
	 
			
	</script>
    <script>
    	var formDataAdministration = new Array (
			new Array ('name_user', 'varchar2', 1)
		);
		function myValidateForm() {
			return validateForm(formDataAdministration);
		}
	</script>
    
	<form action="../crm/call_center/administrationupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return myValidateForm()">
	        <input type="hidden" name="type" value="general">
	        <input type="hidden" name="action" value="add">
	        <input type="hidden" name="process" value="yes">
			<span id="p_span"></span>
	<table <%=Bean.getTableDetailParam() %>>
		
		<tr>
			<td><%= Bean.call_centerXML.getfieldTransl("ID_USER", true) %></td>
			<td>
				<%=Bean.getWindowFindUser("user", "", "", "10") %>
			</td>
			<td><%= Bean.call_centerXML.getfieldTransl("cc_all_question_type", false) %></td>
			<td rowspan="2" align="center">
				<input type="button" value="<%=Bean.calculatorXML.getfieldTransl("add", false) %>" onclick="fromSource()" class="inputfield2"><br>
				<input type="button" value="<%=Bean.calculatorXML.getfieldTransl("delete", false) %>" onclick="fromDestination()" class="inputfield2">
			</td>
			<td><%= Bean.call_centerXML.getfieldTransl("cc_user_question_type", false) %></td>
		</tr>
		<tr>
			<td valign="top"><%= Bean.call_centerXML.getfieldTransl("cc_user_status", true) %></td><td valign="top"><select name="cd_cc_user_status" class="inputfield"><%= Bean.getCallCenterUserStatusOptions("USER", false) %></select></td>
			<td width="20">
				<select name="cd_cc_question_type" id="source" multiple="multiple" size="7" class="inputfield"><%= Bean.getCallCenterQuestionTypeUserOptions("", false) %></select>
			</td>
			<td>
      			
				<select name="cd_cc_question_type" id="destination" multiple="multiple" size="7" class="inputfield"><%= Bean.getCallCenterUserQuestionTypeOptions("", false) %></select>
			</td>
		</tr>
 		<tr>
			<td colspan="4" align="center">
				<%=Bean.getSubmitButtonAjax("../crm/call_center/administrationupdate.jsp") %>
				<%=Bean.getResetButton() %>
				<% if (action.equalsIgnoreCase("add")) { %>
					<%=Bean.getGoBackButton("../crm/call_center/administration.jsp") %>
				<% } else { %>
					<%=Bean.getGoBackButton("../crm/call_center/administrationspecs.jsp?id=" + id) %>
				<% } %>
			</td>
		</tr>

	</table>
	

</form>

	        <%
    	} else {
    	    %> <%= Bean.getUnknownActionText(action) %><%
    	}
	}

else if (process.equalsIgnoreCase("yes"))
	{
    String
    	id_user			 	= Bean.getDecodeParam(parameters.get("id_user")),
    	cd_cc_user_status 	= Bean.getDecodeParam(parameters.get("cd_cc_user_status"));    

    String[] results = new String[2];

    if (action.equalsIgnoreCase("add")) { 
    	
		String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_CALL_CENTER.add_user(?,?,?)}";

		ArrayList<String> pParam = new ArrayList<String>();
			
		pParam.add(id_user);
		pParam.add(cd_cc_user_status);
		
		results = Bean.myCallFunctionParam(callSQL, pParam, 2);
		String resultInt = results[0];
 		String resultMessage = results[1];
    	
	    String resultFull = resultInt;
	    String resultMessageFull = resultMessage;
	    %>
		<%= Bean.showCallSQL(callSQL) %>
		<%
 		if ("0".equalsIgnoreCase(resultInt)) {
 			ArrayList<String> p_value=new ArrayList<String>();
		
    		Set<String> keySet = parameters.keySet();
    		Iterator<String> keySetIterator = keySet.iterator();
    		String key = "";
	    	while(keySetIterator.hasNext()) {
	        	try {
	    			key = (String)keySetIterator.next();
	   				if(key.contains("p_")){
	   					p_value.add(key.substring(2));
	   				}
	    		}
	   			catch(Exception ex){
	   				Bean.writeException(
	   						"../crm/call_center/administrationupdate.jsp",
	   						type,
	   						process,
	   						action,
	   						Bean.commonXML.getfieldTransl("h_get_param_value_error", false)+key+": " + ex.toString());
	   			}
   			}
 		
 			if (p_value.size()>0) {
	 		 for(int counter=0;counter<p_value.size();counter++){
	 			
	 			callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_CALL_CENTER.add_user_question_type(?,?,?)}";
	 				
	 			ArrayList<String> pParam2 = new ArrayList<String>();
	 			
	 			pParam2.add(id_user);
	 			pParam2.add(p_value.get(counter));
				 
				results = Bean.myCallFunctionParam(callSQL, pParam2, 2);
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
   	    <%=Bean.showCallResult(
   	    		callSQL, 
   	    		resultFull, 
   	    		resultMessage,  
   	    	    "../crm/call_center/administrationspecs.jsp?id=" + id_user, 
   	    	    "../crm/call_center/administrationspecs.jsp?id=" + id_user, 
   	    		Bean.form_messageXML.getfieldTransl("add_error", false)) %>
   		<% 
   	} else if (action.equalsIgnoreCase("remove")) { 

	   	String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_CALL_CENTER.delete_user(?,?)}";

	   	ArrayList<String> pParam = new ArrayList<String>();
					
		pParam.add(id_user);

	 	%>
		<%= Bean.getCallResultParam("DELETE", callSQL, pParam, "../crm/call_center/administration.jsp" , "") %>
		<% 	
	} else if (action.equalsIgnoreCase("edit")) { 
		
	 	String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_CALL_CENTER.update_user(?,?,?)}";

	 	ArrayList<String> pParam = new ArrayList<String>();
						
		pParam.add(id);
		pParam.add(cd_cc_user_status);
		
		results = Bean.myCallFunctionParam(callSQL, pParam, 2);
 		String resultInt = results[0];
 		String resultMessage = results[1];
    	
   	    String resultFull = resultInt;
		String resultMessageFull = resultMessage;
		%>
		<%= Bean.showCallSQL(callSQL) %>
		<%
 		
		if ("0".equalsIgnoreCase(resultInt)) {
			ArrayList<String> p_value=new ArrayList<String>();
			
    		Set<String> keySet = parameters.keySet();
    		Iterator<String> keySetIterator = keySet.iterator();
    		String key = "";
	    	while(keySetIterator.hasNext()) {
	        	try {
	    			key = (String)keySetIterator.next();
	   				if(key.contains("p_")){
	   					p_value.add(key.substring(2));
	   				}
	    		}
	   			catch(Exception ex){
	   				Bean.writeException(
	   						"../crm/call_center/administrationupdate.jsp",
	   						type,
	   						process,
	   						action,
	   						Bean.commonXML.getfieldTransl("h_get_param_value_error", false)+key+": " + ex.toString());
	   			}
   			}
 		
 			if (p_value.size()>0) {
	 		 for(int counter=0;counter<p_value.size();counter++){
	 			
	 			callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_CALL_CENTER.add_user_question_type(?,?,?)}";
					
	 			ArrayList<String> pParam2 = new ArrayList<String>();
	 			
				pParam2.add(id);
				pParam2.add(p_value.get(counter));
				 
				results = Bean.myCallFunctionParam(callSQL, pParam2, 2);
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
   	    		resultMessageFull, 
   	    		"../crm/call_center/administrationspecs.jsp?id=" + id_user, 
   	    		"../crm/call_center/administrationspecs.jsp?id=" + id_user, 
   	    		Bean.form_messageXML.getfieldTransl("save_error", false)) %>
   		<% 
	} // кінець умови обробки редагування запису
	     
	     
    else { %> <%= Bean.getUnknownActionText(action) %><% }
	}

else {
    %> <%= Bean.getUnknownProcessText(process) %> <%
}
} else {
    %> <%= Bean.getUnknownTypeText(type) %> <%
}
%>


</body>
</html>
