<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcCRMBean"  />

<%@page import="bc.objects.bcClearingObject"%>
<%@page import="java.util.HashMap"%>
<%@page import="bc.objects.bcClubShortObject"%>
<%@page import="java.util.Set"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.ArrayList"%><html>

<%= Bean.getLogOutScript(request) %>

<head>
	<%= Bean.getMetaContent() %>
	<%= Bean.getBottomFrameCSS() %>
</head>

<body>
<%
HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());

String pageFormName = "FINANCE_CLEARING";

Bean.setJspPageForTabName(pageFormName);

request.setCharacterEncoding("UTF-8");

String id		= Bean.getDecodeParam(parameters.get("id_clearing")); 
String action	= Bean.getDecodeParam(parameters.get("action")); 
String process	= Bean.getDecodeParam(parameters.get("process"));
String type		= Bean.getDecodeParam(parameters.get("type"));

if (id==null || ("".equalsIgnoreCase(id))) id="empty";
if (type==null || ("".equalsIgnoreCase(type))) type="empty";
if (action==null || ("".equalsIgnoreCase(action))) action="empty";
if (process==null || ("".equalsIgnoreCase(process))) process="empty";

if (type.equalsIgnoreCase("header")) {
	if (process.equalsIgnoreCase("no")) {
		if (action.equalsIgnoreCase("export")) {
			
			bcClearingObject clearing = new bcClearingObject(id);
    	    %> 
			<%= Bean.getOperationTitle(
					Bean.clearingXML.getfieldTransl("h_export", false),
					"Y",
					"N") 
			%>
			<script>
				var formData = new Array (
					new Array ('exp_file', 'varchar2', 1)
				);
			</script>
			
			<form action="../crm/finance/clearingupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return validateForm(formData)">
			    <input type="hidden" name="type" value="export">
			    <input type="hidden" name="action" value="run">
			    <input type="hidden" name="process" value="yes">
				<input type="hidden" name="id_clearing" value="<%= id %>">
				<input type="hidden" name="ID_CLEARING" value="<%= id %>">
				<input type="hidden" name="REPORT_ID" value="37">
				<input type="hidden" name="REPORT_FORMAT" value="<%= Bean.getReportFormat() %>">
		    <table <%=Bean.getTableDetailParam() %>>
		    <tr>   
		       <td align="left" width="50%"> 
		          <font size=2><%= Bean.clearingXML.getfieldTransl("number_clearing", false) %>:&nbsp;</font>
		       </td>
		       <td align="left"> 
		          <input type="text" name="number_clearing" size="20" value="<%=clearing.getValue("NUMBER_CLEARING") %>" readonly="readonly" class="inputfield-ro">
		          
		       </td>
		    </tr>
		    <tr>
		       <td align="left" width="50%"> 
		          <font size=2><%= Bean.clearingXML.getfieldTransl("h_owner", false) %></font>
		       </td>
		       <td align="left"><select name="ID_OWNER" class="inputfield"><%= Bean.getClearingBankAccountOwnerOptions(id, "", true) %></select></td>
		    </tr>
		    <tr>
		       <td align="left" width="50%"> 
		          <font size=2><%= Bean.postingXML.getfieldTransl("export_with_some_bank_account", false) %></font>
		       </td>
		       <td align="left"><select name="export_some_accounts" class="inputfield"><%= Bean.getMeaningFromLookupNameOptions("YES_NO", Bean.getUIUserParam("CLEARING_EXPORT_SOME_ACCOUNTS"), false) %></select></td>
		    </tr>
		    <tr>
		       <td align="left" width="50%"> 
		          <font size=2><%= Bean.postingXML.getfieldTransl("export_0_amount", false) %></font>
		       </td>
		       <td align="left" ><select name="export_0_amount" class="inputfield"><%= Bean.getMeaningFromLookupNameOptions("YES_NO", Bean.getUIUserParam("CLEARING_EXPORT_0_AMOUNT"), false) %></select></td>
		    </tr>
		    <tr>
		       <td align="left" width="50%"> 
		          <font size=2><%= Bean.postingXML.getfieldTransl("debug", false) %></font>
		       </td>
		       <td align="left"><select name="debug" class="inputfield"><%= Bean.getMeaningFromLookupNameOptions("YES_NO", Bean.getUIUserParam("CLEARING_EXPORT_DEBUG"), false) %></select></td>
		    </tr>

		    <tr>
		        <td align="center" colspan="2">
					<%=Bean.getSubmitButtonAjax("../crm/finance/clearingupdate.jsp", "export", "updateForm") %>
					<%=Bean.getResetButton() %>
					<%=Bean.getGoBackButton("../crm/finance/clearingspecs.jsp?id="+id) %>
		        </td>
		    </tr>
		 </table>
		 </form>
			<%
			
		}
	} else if (process.equalsIgnoreCase("yes")) {
		
		if (action.equalsIgnoreCase("remove")) {
			
			String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_CLEARING.delete_clearing(?,?)}";

			String[] pParam = new String [1];

			pParam[0] = id;
			
		 	%>
			<%= Bean.getCallResultParam("DELETE", callSQL, pParam, "../crm/finance/clearing.jsp" , "") %>
			<% 	

		} else if (action.equalsIgnoreCase("edit")) {

			String 
				number_clearing		= Bean.getDecodeParam(parameters.get("number_clearing")),
				state_clearing		= Bean.getDecodeParam(parameters.get("state_clearing"));
	    
			String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_CLEARING.update_clearing(?,?,?,?)}";

			String[] pParam = new String [3];

			pParam[0] = id;
			pParam[1] = number_clearing;
			pParam[2] = state_clearing;
			
		 	%>
			<%= Bean.getCallResultParam("UPDATE", callSQL, pParam, "../crm/finance/clearingspecs.jsp?id=" + id, "") %>
			<% 	

		} else { %> 
			<%= Bean.getUnknownActionText(action) %><% 
		}
	} else {
	    %> <%= Bean.getUnknownProcessText(process) %> <%
	}
} else if (type.equalsIgnoreCase("set_state")) {
 
	if (process.equalsIgnoreCase("yes")) {
    
		String
    		l_bank_statement	= Bean.getDecodeParam(parameters.get("bank_statement"));

    	if (action.equalsIgnoreCase("edit")) { 
    		
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
	   						"../crm/finance/clearingupdate.jsp",
	   						type,
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
	   	    
	   	 	String[] pParam = new String [3];
	   	    
		   	 if (id_value.size()>0) {
	  	 		 for(int counter=0;counter<id_value.size();counter++){ 
	  	 			 if (!(prv_value.contains(id_value.get(counter)))) {
	  	 				 
	  	 				callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_CLEARING.set_need_clearing_line_export(?,?,?,?)}";
	   					
	   					pParam[0] = id;
	   					pParam[1] = id_value.get(counter);
	   					pParam[2] = "Y";
				
						results = Bean.myCallFunctionParam(callSQL, pParam, 1);
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
					   	 				 
					 		callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_CLEARING.set_need_clearing_line_export('"+
		   					id + "','" + prv_value.get(counter) + "','N',?)}";
		   					
		   					pParam[0] = id;
		   					pParam[1] = prv_value.get(counter);
		   					pParam[2] = "N";
					
							results = Bean.myCallFunctionParam(callSQL, pParam, 1);
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
		   	    		"../crm/finance/clearingspecs.jsp?id=" + id, 
		   	    		"../crm/finance/clearingspecs.jsp?id=" + id, 
		   	    		Bean.form_messageXML.getfieldTransl("save_error", false)) %>
		   		<% 
    		
       	} else { %> 
       		<%= Bean.getUnknownActionText(action) %><% 
    	}
	} else {
    	%> <%= Bean.getUnknownProcessText(process) %> <%
 	}

} else if (type.equalsIgnoreCase("create")) {
	if (process.equalsIgnoreCase("no")) {
		if (action.equalsIgnoreCase("run")) {
			
    	    %> 
			<%= Bean.getOperationTitle(
					Bean.clearingXML.getfieldTransl("LAB_RUN_CLEARING", false),
					"N",
					"N") 
			%>
			<script>
				var formData = new Array (
					new Array ('exp_file', 'varchar2', 1)
				);
				function setPeriodExportType() {
					document.getElementById("export_type_period").checked = true;
				}
			</script>
			
			<form action="../crm/finance/clearingupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return validateForm(formData)">
			    <input type="hidden" name="type" value="create">
			    <input type="hidden" name="action" value="run">
			    <input type="hidden" name="process" value="yes">
		    <table <%=Bean.getTableDetailParam() %>>
		    <tr>
				<%
		    		bcClubShortObject club = new bcClubShortObject(Bean.getCurrentClubID());
				%>

	 		    <td><font size=2><%= Bean.clubXML.getfieldTransl("club", false) %></font>
					<%= Bean.getGoToClubLink(club.getValue("ID_CLUB")) %>
				</td>
			  	<td>
					<%=Bean.getWindowFindClub("club", club.getValue("ID_CLUB"), club.getValue("SNAME_CLUB"), "25") %>
			  	</td>
		    </tr>
		    <tr>
		       <td align="left" width="50%"> 
		          <font size=2><%= Bean.postingXML.getfieldTransl("debug", false) %></font>
		       </td>
		       <td align="left"><select name="debug" class="inputfield"><%= Bean.getMeaningFromLookupNameOptions("YES_NO", Bean.getUIUserParam("CLEARING_EXPORT_DEBUG"), false) %></select></td>
		    </tr>
		    <tr>   
		       <td align="left" colspan="2">
				  <input type="radio" name="export_type" id="export_type_all" size="20" CHECKED value="ALL" class="inputfield"> 
		          <label for="export_type_all"><%= Bean.clearingXML.getfieldTransl("h_create_all", false) %>:&nbsp;</label>
		       </td>
		    </tr>
		    <tr>   
		       	<td align="left" width="50%">
				  	<input type="radio" name="export_type" id="export_type_period" size="20" value="PERIOD" class="inputfield"> 
		          	<label for="export_type_period" ><%= Bean.clearingXML.getfieldTransl("h_create_for_period", false) %>:&nbsp;</label>
		       	</td>
			   	<td align="left">
					<font size=2><%= Bean.commonXML.getfieldTransl("h_period_from", false) %>:&nbsp;</font>
					<%=Bean.getCalendarInputField("begin_period", "", "10", "setPeriodExportType()", false) %>
					<font size=2><%= Bean.commonXML.getfieldTransl("h_period_to", false) %>:&nbsp;</font>
					<%=Bean.getCalendarInputField("end_period", "", "10", "setPeriodExportType()", false) %>
				</td>
		    </tr>

		    <tr>
		        <td align="center" colspan="2">
					<%=Bean.getSubmitButtonAjax("../crm/finance/clearingupdate.jsp", "execute", "updateForm") %>
					<%=Bean.getResetButton() %>
					<%=Bean.getGoBackButton("../crm/finance/clearing.jsp") %>
		        </td>
		    </tr>
		 </table>
		 </form>
		<%= Bean.getCalendarScript("begin_period", false) %>
		<%= Bean.getCalendarScript("end_period", false) %>
			
		<%
			
		}
	} else if (process.equalsIgnoreCase("yes")) {
		if (action.equalsIgnoreCase("run")) {
			
			String 
				debug			= Bean.getDecodeParam(parameters.get("debug")),
				export_type		= Bean.getDecodeParam(parameters.get("export_type")),
				begin_period	= Bean.getDecodeParam(parameters.get("begin_period")),
				end_period		= Bean.getDecodeParam(parameters.get("end_period")),
				id_club			= Bean.getDecodeParam(parameters.get("id_club"));
			
			if ("ALL".equalsIgnoreCase(export_type)) {
				begin_period = "";
				end_period = "";
			}
	    
	    	String[] results = new String[2];
			%> 

			<%= Bean.form_messageXML.getfieldTransl("processing_update", false) %>
			<%
			String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_CLEARING.run_clearing(?,?,?,?,?,?,?,?)}";

			String[] pParam = new String [5];

			pParam[0] = id_club;
			pParam[1] = begin_period;
			pParam[2] = end_period;
			pParam[3] = debug;
			pParam[4] = Bean.getDateFormat();
			
			results = Bean.myCallFunctionParam(callSQL, pParam, 4);
			String resultInt = results[0];
			String resultMessage = results[1];
			String clearingID = results[2];
			String reportID = results[3];
			System.out.println("../crm/finance/clearingspecs.jsp?id="+clearingID);
				
			%>
	   	    <%=Bean.showCallResult(
	   	    		callSQL, 
	   	    		resultInt, 
	   	    		resultMessage, 
	   	    		"../crm/finance/clearingspecs.jsp?id="+clearingID, 
	   	    		"../crm/finance/clearing.jsp", 
	   	    		Bean.form_messageXML.getfieldTransl("save_error", false)) %>
	   		<% 
			
		} else { %> <%= Bean.getUnknownActionText(action) %><% 

		}
	} else {
	    %> <%= Bean.getUnknownProcessText(process) %> <%
	}
} else if (type.equalsIgnoreCase("export")) {
	if (process.equalsIgnoreCase("yes")) {
		if (action.equalsIgnoreCase("run")) {
			
			String 
				id_owner				= Bean.getDecodeParam(parameters.get("ID_OWNER")),
				debug					= Bean.getDecodeParam(parameters.get("debug")),
				export_some_accounts 	= Bean.getDecodeParam(parameters.get("export_some_accounts")),
				export_0_amount 		= Bean.getDecodeParam(parameters.get("export_0_amount"));
	    
			/*String path_to_file = "c:\\temp\\text.pdf";
			String fileDirectory = "C:\\BonusClub\\tomcat\\webapps\\BonusDemo\\reports\\templates_jasper\\";
			Reporter rep = new Reporter();
			if(rep.generateReport(
					   request, 
					   response, 
					   fileDirectory,
					   path_to_file)==true){
				
			}
			*/
	    	String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_CLEARING.run_clearing_export(?,?,?,?,?,?)}";

			String[] pParam = new String [5];

			pParam[0] = id;
			pParam[1] = id_owner;
			pParam[2] = export_some_accounts;
			pParam[3] = export_0_amount;
			pParam[4] = debug;
			
		 	%>
			<%= Bean.getCallResultParam("UPDATE", callSQL, pParam, "../crm/finance/clearingspecs.jsp?id=" + id, "") %>
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
