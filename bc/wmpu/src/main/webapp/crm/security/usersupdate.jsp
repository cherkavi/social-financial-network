<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcCRMBean"  />
<%@page import="java.util.Enumeration"%>
<%@page import="java.util.ArrayList"%>
<%@page import="bc.connection.Connector"%>
<%@page import="bc.objects.bcUserObject"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Set"%>
<%@page import="java.util.Iterator"%><html>

<%= Bean.getLogOutScript(request) %>

<head>
	<%= Bean.getMetaContent() %>
	<%= Bean.getBottomFrameCSS() %>
</head>

<body>
<%
HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());
System.out.println(parameters);

String pageFormName = "SECURITY_USERS";

Bean.setJspPageForTabName(pageFormName);

request.setCharacterEncoding("UTF-8");

String id		= Bean.getDecodeParamPrepare(parameters.get("id"));
String action	= Bean.getDecodeParamPrepare(parameters.get("action")); 
String process	= Bean.getDecodeParamPrepare(parameters.get("process"));

if (process.equalsIgnoreCase("no"))
/* вибираємо тип дії (добавити, видалити...)*/
	{
	   /*  --- Добавити запис --- */
    	if (action.equalsIgnoreCase("add") || action.equalsIgnoreCase("add2")) {
	        %> 
			<%= Bean.getOperationTitle(
					Bean.userXML.getfieldTransl("LAB_ADD_USER", false),
					"Y",
					"N") 
			%>
		<script>
			var formData = new Array (
				new Array ('NAME_USER', 'varchar2', 1),
				new Array ('PASSWORD', 'varchar2', 1),
				new Array ('CONFIRM_PASSWORD', 'varchar2', 1),
				new Array ('CD_USER_STATUS', 'varchar2', 1)
			);
			function myValidateForm(){

				var select, form;
				form = document.getElementById('updateForm');
				select = document.getElementById('list_ip');
				try {
					if (select.options.length > 0) {
			    		var elem = document.createElement('input');
						elem.type = 'hidden';
						elem.name = 'ipcount';
						elem.value = select.options.length;
						form.appendChild(elem);
				  		for (i=0; i < select.options.length; i++) {
				    		var elem = document.createElement('input');
							elem.type = 'hidden';
							elem.name = 'ip_'+i;
							elem.value = select.options[i].text;
							form.appendChild(elem);
				  		}
					}
				} catch (e) {alert(e);}
				return validateForm(formData);
			}

			function addElementToSelect(id, value) {
				var select, option;
				if (!checkIP(value)) {
					return false;
				}
				select = document.getElementById(id);
				for (i=0; i < select.options.length; i++) {
					if (validIp == select.options[i].text) {
						return false;
					}
				}
				option = document.createElement('option');
				option.value = option.text = validIp;
				select.appendChild(option);
				return true;
			}
			var validIp = '';
			function addIp() {
				if (addElementToSelect('list_ip', document.getElementById('new_ip').value)) {
				}
			}
			function removeIp() {
				var select, current;
				select = document.getElementById('list_ip');
				current = select.selectedIndex;
				//alert(current);
				if (current != -1) {
					select.options.remove(current);
				}
			}
			function checkIP(ip) {
				validIp = '';
			    var x = ip.split("."), x1, x2, x3, x4;

			    if (x.length == 4) {
			        x1 = parseInt(x[0], 10);
			        x2 = parseInt(x[1], 10);
			        x3 = parseInt(x[2], 10);
			        x4 = parseInt(x[3], 10);

			        if (isNaN(x1) || isNaN(x2) || isNaN(x3) || isNaN(x4)) {
			            return false;
			        }

			        if ((x1 >= 0 && x1 <= 255) && (x2 >= 0 && x2 <= 255) && (x3 >= 0 && x3 <= 255) && (x4 >= 0 && x4 <= 255)) {
			        	validIp = x1 + '.' + x2 + '.' + x3 + '.' + x4;
			            return true;
			        }
			    }
			    alert("You have entered an invalid IP address!");
			    return false;
			}
		</script>
        <form action="../crm/security/usersupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" >
	        <input type="hidden" name="action" value="add">
	        <input type="hidden" name="process" value="yes">

		<table <%=Bean.getTableDetailParam() %>>
		<tr>
			<td><%= Bean.userXML.getfieldTransl("NAME_USER", true) %> </td><td><input type="text" name="NAME_USER" size="20" value="" class="inputfield"></td>
			<td rowspan="5"><%= Bean.userXML.getfieldTransl("permit_ip", false) %> </td>
			<td rowspan="5">
				<select id="list_ip" name="list_ip" size="4" class="inputfield" style="width: 200px !important;;overflow-y: scroll; height: auto !important;" ></select>
				<span style="width: 100px; vertical-align: top; display: inline-block; height: 50px ! important;">
					<input type="button" class="inputfield" style="width:30px !important" value="-" onclick="removeIp();">
				</span>
				<div id="add_ip"><input type="text" id="new_ip" class="inputfield" size="20"><input type="button" class="inputfield" value="Добавить" onclick="addIp();"></div>
			</td>
		</tr>
	    <tr>
			<td><span id="span_password"><%= Bean.userXML.getfieldTransl("password", true) %></span></td><td><input type="password" name="PASSWORD" size="20" value="" class="inputfield"></td>
		</tr>	
		<tr>
			<td><span id="span_confirm_password"><%= Bean.userXML.getfieldTransl("confirm_password", true) %></span></td><td><input type="password" name="CONFIRM_PASSWORD" size="20" value="" class="inputfield"></td>
		</tr>	
		<tr>
			<td><%= Bean.userXML.getfieldTransl("CD_USER_STATUS", true) %></td> <td><select name="CD_USER_STATUS" id='user_status' class="inputfield"><%=Bean.getUserStatusOptions("OPENED", true) %></select></td>
		</tr>		
		<tr>
			<td><%=Bean.userXML.getfieldTransl("fio_nat_prs", false)%></td>
			<td>
				<%=Bean.getWindowFindNatPrsRole("nat_prs_role", "", "", "40") %>
			</td>
		</tr>	
		<tr>
			<td><%= Bean.userXML.getfieldTransl("DESC_USER", false) %></td> <td><textarea name="DESC_USER" cols="47" rows="3" class="inputfield"></textarea></td>
		<tr>
			<td colspan="2">&nbsp;</td>
		</tr>
		<tr>
			<td colspan="6" align="center">
				<%=Bean.getSubmitButtonAjax("../crm/security/usersupdate.jsp") %>
				<%=Bean.getResetButton() %>
				<% if (action.equalsIgnoreCase("add")) { %>
					<%=Bean.getGoBackButton("../crm/security/users.jsp") %>
				<% } else { %>
					<%=Bean.getGoBackButton("../crm/security/userspecs.jsp?id=" + id) %>
				<% } %>
			</td>
		</tr>


	</table>

</form>
	        <%
   	} else {
   	    %> <%= Bean.getUnknownActionText(action) %><%
   	}

} else if (process.equalsIgnoreCase("yes"))	{
    
	String  
    	NAME_USER				= Bean.getDecodeParam(parameters.get("NAME_USER")),
    	DESC_USER				= Bean.getDecodeParam(parameters.get("DESC_USER")),
    	PASSWORD				= Bean.getDecodeParam(parameters.get("PASSWORD")),
    	CONFIRM_PASSWORD		= Bean.getDecodeParam(parameters.get("CONFIRM_PASSWORD")),
    	NAME_MODULE_TYPE		= Bean.getDecodeParam(parameters.get("NAME_MODULE_TYPE")),
    	CD_MODULE_TYPE			= Bean.getDecodeParam(parameters.get("NAME_MODULE_TYPE")),
    	ID_VIRTUAL_TERM			= Bean.getDecodeParam(parameters.get("ID_VIRTUAL_TERM")),
    	CD_USER_STATUS			= Bean.getDecodeParam(parameters.get("CD_USER_STATUS")),
    	DATE_STATUS				= Bean.getDecodeParam(parameters.get("DATE_STATUS")),
    	EXIST_FLAG				= Bean.getDecodeParam(parameters.get("EXIST_FLAG")),
    	phone_work				= Bean.getDecodeParam(parameters.get("phone_work")),
    	phone_mobile			= Bean.getDecodeParam(parameters.get("phone_mobile")),
    	fax						= Bean.getDecodeParam(parameters.get("fax")),
    	web_site				= Bean.getDecodeParam(parameters.get("web_site")),
    	email					= Bean.getDecodeParam(parameters.get("email")),
    	id_nat_prs_role			= Bean.getDecodeParam(parameters.get("id_nat_prs_role")),
    	ipcount					= Bean.getDecodeParam(parameters.get("ipcount"));
    
	ArrayList<String> ip=new ArrayList<String>();
	
	Set<String> keySetIP = parameters.keySet();
	Iterator<String> keySetIteratorIP = keySetIP.iterator();
	String keyIP = "";
	while(keySetIteratorIP.hasNext()) {
			try{
				keyIP = (String)keySetIteratorIP.next();
				if(keyIP.startsWith("ip_")){
					ip.add(parameters.get(keyIP));
				}
			}
			catch(Exception ex){
				Bean.writeException(
						"../crm/security/usersupdate.jsp",
						"",
						process,
						action,
						Bean.commonXML.getfieldTransl("h_get_param_value_error", false)+keyIP+": " + ex.toString());
			}
		}
	if (action.equalsIgnoreCase("add")) { 
    	
    	ArrayList<String> pParam = new ArrayList<String>();
    	
    	String resultInt = "";
   	    String resultFull = "0";
   	    String resultMessage = "";
   	    String resultMessageFull = "";
   	    String[] results = new String[3];
   	    
   	    pParam.add(NAME_USER);
		pParam.add(PASSWORD);
		pParam.add(CONFIRM_PASSWORD);
		pParam.add(DESC_USER);
		pParam.add(CD_USER_STATUS);
		pParam.add(id_nat_prs_role);
		
		String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK$USER_UI.add_user(?,?,?,?,?,?,?,?)}";
		
    	results = Bean.myCallFunctionParam(callSQL, pParam, results.length);
		resultInt = results[0];
		id = results[1];
		resultMessage = results[2];
		
		%>
		<%= Bean.showCallSQL(callSQL) %>
		<%
	
		if (!("0".equalsIgnoreCase(resultInt))) {
			resultFull = resultInt;
			resultMessageFull = resultMessageFull + "; " +resultMessage;
		}
		
		if (ip.size()>0) {
			String[] pParam2 = new String [2];
  	 		for(int counter=0;counter<ip.size();counter++){ 
  	 			callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK$USER_UI.set_user_permit_ip(?,?,?)}";
		        		
  	 			pParam2[0] = id;
  	 			pParam2[1] = ip.get(counter);
			
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

   	 	%>
  	    <%=Bean.showCallResult(
   	    		callSQL, 
   	    		resultFull, 
   	    		resultMessageFull, 
   	    		"../crm/security/userspecs.jsp?id=" + id, 
   	    		"../crm/security/userspecs.jsp?id=" + id, 
   	    		Bean.form_messageXML.getfieldTransl("save_error", false)) %>
   		<% 

    } else if (action.equalsIgnoreCase("remove")) { 
    
    	if (id.equalsIgnoreCase(Bean.loginUser.getValue("ID_USER"))) {
    		%>
			<center> <b><%= Bean.form_messageXML.getfieldTransl("remove_error", false) %></b></center><br>
			<font color="red"><%= Bean.userXML.getfieldTransl("h_current_user_cannot_be_deleted", false) %></font> <br>
			<center><font color="blue"><u><a href="#" onclick="ajaxpage('../crm/security/users.jsp','div_main')"><%= Bean.buttonXML.getfieldTransl("go_back", false) %></a></u></font></center>
			<script language="JavaScript"> window.alert('<%= Bean.form_messageXML.getfieldTransl("remove_error", false) %>'); </script>
		 		<%
    	} else {
    		
	    	bcUserObject user = new bcUserObject(id);
		
			Connector.dropUserConnection(user.getValue("NAME_USER"));
	
			ArrayList<String> pParam = new ArrayList<String>();
			
			pParam.add(id);
			pParam.add("Y");

		 	%>
			<%= Bean.executeDeleteFunction("PACK$USER_UI.delete_user", pParam, "../crm/security/users.jsp" , "") %>
			<% 	

    	}
     
    } else if (action.equalsIgnoreCase("edit")) { 
    	
    	if ("DELETED".equalsIgnoreCase(CD_USER_STATUS)) {
    		Connector.dropUserConnection(NAME_USER);
    	}
    	
    	ArrayList<String> pParam = new ArrayList<String>();
    	
    	String resultInt = "";
   	    String resultFull = "0";
   	    String resultMessage = "";
   	    String resultMessageFull = "";
   	    String[] results = new String[2];
    	
    	pParam.add(id);
		pParam.add(PASSWORD);
		pParam.add(CONFIRM_PASSWORD);
		pParam.add(DESC_USER);
		pParam.add(CD_USER_STATUS);
		pParam.add(id_nat_prs_role);
		
		String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK$USER_UI.update_user(?,?,?,?,?,?,?)}";
		
    	results = Bean.myCallFunctionParam(callSQL, pParam, results.length);
		resultInt = results[0];
		resultMessage = results[1];
		
		%>
		<%= Bean.showCallSQL(callSQL) %>
		<%
	
		if (!("0".equalsIgnoreCase(resultInt))) {
			resultFull = resultInt;
			resultMessageFull = resultMessageFull + "; " +resultMessage;
		} else {
			pParam.clear();
			pParam.add(id);
			
			callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK$USER_UI.delete_all_user_permit_ip(?,?)}";
			
	    	results = Bean.myCallFunctionParam(callSQL, pParam, results.length);
			resultInt = results[0];
			resultMessage = results[1];
			
			%>
			<%= Bean.showCallSQL(callSQL) %>
			<%
		
			if (!("0".equalsIgnoreCase(resultInt))) {
				resultFull = resultInt;
				resultMessageFull = resultMessageFull + "; " +resultMessage;
			} else {
				if (ip.size()>0) {
					String[] pParam2 = new String [2];
		  	 		for(int counter=0;counter<ip.size();counter++){ 
		  	 			callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK$USER_UI.set_user_permit_ip(?,?,?)}";
				        		
		  	 			pParam2[0] = id;
		  	 			pParam2[1] = ip.get(counter);
					
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
   		}
   	 	%>
  	    <%=Bean.showCallResult(
   	    		callSQL, 
   	    		resultFull, 
   	    		resultMessageFull, 
   	    		"../crm/security/userspecs.jsp?id=" + id, 
   	    		"../crm/security/userspecs.jsp?id=" + id, 
   	    		Bean.form_messageXML.getfieldTransl("save_error", false)) %>
   		<% 

	 	%>
		<% 	

    } else if (action.equalsIgnoreCase("set_jur_prs")) {%>
    	
   		<%= Bean.form_messageXML.getfieldTransl("processing_update", false) %>
   	   	<% 
    
    	ArrayList<String> id_value=new ArrayList<String>();
		ArrayList<String> prv_value=new ArrayList<String>();

    	String callSQL = "";
    	Set<String> keySet = parameters.keySet();
		Iterator<String> keySetIterator = keySet.iterator();
		String key = "";
    	while(keySetIterator.hasNext()) {
   			try{
   				key = (String)keySetIterator.next();
   				if(key.contains("chb_id")){
   					id_value.add(key.substring(7));
   				}
   				if(key.contains("prv_id")){
   					prv_value.add(key.substring(7));
   				}
   			}
   			catch(Exception ex){
   				Bean.writeException(
   						"../crm/security/usersupdate.jsp",
   						"",
   						process,
   						action,
   						Bean.commonXML.getfieldTransl("h_get_param_value_error", false)+key+": " + ex.toString());
   			}
   		}
    	

   	    String resultInt = "";
   	    String resultFull = "0";
   	    String resultMessage = "";
   	    String resultMessageFull = "";
   	    String[] results = new String[4];
   	    
   	 	String[] pParam2 = new String [3];

   	    if (id_value.size()>0) {
  	 		 for(int counter=0;counter<id_value.size();counter++){ 
  	 			 if (!(prv_value.contains(id_value.get(counter)))) {
  	 				String id_club = id_value.get(counter).substring(0,id_value.get(counter).indexOf("_"));
  	 				String id_jur_prs = id_value.get(counter).substring(id_value.get(counter).indexOf("_")+1);
  	 				
  	 				callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK$USER_UI.add_user_jur_prs(?,?,?,?)}";
		        		
  	 				pParam2[0] = id;
  	 				pParam2[1] = id_club;
  	 				pParam2[2] = id_jur_prs;
			
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
   	 	if (prv_value.size()>0) {
   	 		for(int counter=0;counter<prv_value.size();counter++){ 
			 	if (!(id_value.contains(prv_value.get(counter)))) {
			 		String id_club = prv_value.get(counter).substring(0,prv_value.get(counter).indexOf("_"));
  	 				String id_jur_prs = prv_value.get(counter).substring(prv_value.get(counter).indexOf("_")+1);
  	 							 
		        	callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK$USER_UI.delete_user_jur_prs(?,?,?,?)}";
		        		
		        	pParam2[0] = id;
		        	pParam2[1] = id_club;
		        	pParam2[2] = id_jur_prs;
				
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
   	    		resultMessageFull, 
   	    		"../crm/security/userspecs.jsp?id=" + id, 
   	    		"../crm/security/userspecs.jsp?id=" + id, 
   	    		Bean.form_messageXML.getfieldTransl("save_error", false)) %>
   		<% 

    } else if (action.equalsIgnoreCase("set_club_priv")) {%>
	
		<%= Bean.form_messageXML.getfieldTransl("processing_update", false) %>
	   	<% 

	ArrayList<String> id_value=new ArrayList<String>();
	ArrayList<String> prv_value=new ArrayList<String>();

	String callSQL = "";
	Set<String> keySet = parameters.keySet();
	Iterator<String> keySetIterator = keySet.iterator();
	String key = "";
	while(keySetIterator.hasNext()) {
			try{
				key = (String)keySetIterator.next();
				if(key.contains("chb_id")){
					id_value.add(key.substring(7));
				}
				if(key.contains("prv_id")){
					prv_value.add(key.substring(7));
				}
			}
			catch(Exception ex){
				Bean.writeException(
						"../crm/security/usersupdate.jsp",
						"",
						process,
						action,
						Bean.commonXML.getfieldTransl("h_get_param_value_error", false)+key+": " + ex.toString());
			}
		}

	    String resultInt = "";
	    String resultFull = "0";
	    String resultMessage = "";
	    String resultMessageFull = "";
	    String[] results = new String[4];
	    
	    String[] pParam2 = new String [2];

	    if (id_value.size()>0) {
	 		 for(int counter=0;counter<id_value.size();counter++){ 
	 			 if (!(prv_value.contains(id_value.get(counter)))) {
	 				 
	        	callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK$USER_UI.add_user_club(?,?,?)}";
		
	        	pParam2[0] = id;
	        	pParam2[1] = id_value.get(counter);
			
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
	 	if (prv_value.size()>0) {
	 		for(int counter=0;counter<prv_value.size();counter++){ 
		 	if (!(id_value.contains(prv_value.get(counter)))) {
		   	 				 
	        	callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK$USER_UI.delete_user_club(?,?,?)}";
	        		
	        	pParam2[0] = id;
	        	pParam2[1] = prv_value.get(counter);
				
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
	    		resultMessageFull, 
	    		"../crm/security/userspecs.jsp?id=" + id, 
	    		"../crm/security/userspecs.jsp?id=" + id, 
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
