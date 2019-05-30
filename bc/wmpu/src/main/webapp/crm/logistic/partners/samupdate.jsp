<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcCRMBean"  />

<%= Bean.getLogOutScript(request) %>

<%@page import="java.util.HashMap"%>
<%@page import="bc.objects.bcLGObject"%>
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

String pageFormName = "LOGISTIC_PARTNERS_SAMS";
String tagSAMEdit = "_SAM_EDIT";

Bean.setJspPageForTabName(pageFormName);

request.setCharacterEncoding("UTF-8");
String id		= Bean.getDecodeParam(parameters.get("id")); 
String line		= Bean.getDecodeParam(parameters.get("line")); 
String type		= Bean.getDecodeParam(parameters.get("type")); 
String action	= Bean.getDecodeParam(parameters.get("action")); 
String process	= Bean.getDecodeParam(parameters.get("process"));

if (id==null || ("".equalsIgnoreCase(id))) id="empty";
if (line==null || ("".equalsIgnoreCase(line))) line="empty";
if (type==null || ("".equalsIgnoreCase(type))) type="empty";
if (action==null || ("".equalsIgnoreCase(action))) action="empty";
if (process==null || ("".equalsIgnoreCase(process))) process="empty";

if (type.equalsIgnoreCase("general")) {
	if (process.equalsIgnoreCase("no"))
	/* вибираємо тип дії (добавити, видалити...)*/
	{
	   /*  --- Добавити запис --- */
    	if (action.equalsIgnoreCase("add") || action.equalsIgnoreCase("add2")) {
    		
    		bcClubShortObject club = new bcClubShortObject(Bean.getCurrentClubID());

	        %>
	<script>
		var formData = new Array (
			new Array ('action_date', 'varchar2', 1),
			new Array ('name_club', 'varchar2', 1)
		);
	</script> 
		<%= Bean.getOperationTitle(
	    		Bean.logisticXML.getfieldTransl("h_action_sam_add", false),
				"Y",
				"Y") 
		%>
    <form action="../crm/logistic/partners/samupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return validateForm(formData)">
        <input type="hidden" name="action" value="add">
        <input type="hidden" name="process" value="yes">
    	<input type="hidden" name="type" value="general">
	<table <%=Bean.getTableDetailParam() %>>
		<tr>
			<td><%= Bean.logisticXML.getfieldTransl("name_lg_type", false) %></td><td><input type="text" name="name_lg_type" size="60" value="<%=Bean.getLGTypeName("SAM") %>" readonly="readonly" class="inputfield-ro"></td>
 		    <td><%= Bean.clubXML.getfieldTransl("club", true) %>
				<%= Bean.getGoToClubLink(club.getValue("ID_CLUB")) %>
			</td>
		  	<td>
				<%=Bean.getWindowFindClub("club", club.getValue("ID_CLUB"), club.getValue("SNAME_CLUB"), "25") %>
	  		</td>
		</tr>
		<tr>
			<td><%= Bean.logisticXML.getfieldTransl("action_date_frmt", true) %></td><td><%=Bean.getCalendarInputField("action_date", Bean.getSysDate(), "10") %></td>
			<td class="top_line"><%= Bean.logisticXML.getfieldTransl("sname_jur_prs_receiver", false) %></td>
			<td class="top_line">
				<%=Bean.getWindowFindJurPrs("jur_prs_receiver", "", "", "ALL", "40") %>
			</td>			
		</tr>
		<tr>
			<td rowspan="3"><%=  Bean.logisticXML.getfieldTransl("operation_desc", false) %></td><td rowspan="3"><textarea name="operation_desc" cols="60" rows="6" class="inputfield"></textarea></td>
			<td><%= Bean.logisticXML.getfieldTransl("id_service_place_receiver", false) %></td>
			<td>
				<%=Bean.getWindowFindServicePlace("service_place_receiver", "", "", "'+document.getElementById('id_jur_prs_receiver').value+'", "", "40") %>
			</td>			
		</tr>
		<tr>
			<td><%= Bean.logisticXML.getfieldTransl("id_contact_prs_receiver", false) %></td>
			<td>
				<%=Bean.getWindowContactPersons("contact_prs_receiver", "", "'+document.getElementById('id_jur_prs_receiver').value+'", "'+document.getElementById('id_service_place_receiver').value+'", "40") %>
			</td>			
		</tr>
		<tr>
			<td><%=  Bean.logisticXML.getfieldTransl("desc_receiver", false) %></td><td><textarea name="desc_receiver" cols="60" rows="3" class="inputfield"></textarea></td>
		</tr>
		<tr>
			<td><%= Bean.logisticXML.getfieldTransl("sam_count", false) %></td><td><input type="text" name="object_count" size="20" value="" class="inputfield"></td>
			<td class="top_line"><%= Bean.logisticXML.getfieldTransl("sname_jur_prs_sender", false) %></td>
			<td class="top_line">
				<%=Bean.getWindowFindJurPrs("jur_prs_sender", "", "", "ALL", "40") %>
			</td>			
		</tr>
		<tr>
			<td colspan="2">&nbsp;</td>
			<td><%= Bean.logisticXML.getfieldTransl("id_service_place_sender", false) %></td>
			<td>
				<%=Bean.getWindowFindServicePlace("service_place_sender", "", "", "'+document.getElementById('id_jur_prs_sender').value+'", "", "40") %>
			</td>			
		</tr>
		<tr>
			<td colspan="2">&nbsp;</td>
			<td><%= Bean.logisticXML.getfieldTransl("id_contact_prs_sender", false) %></td>
			<td>
				<%=Bean.getWindowContactPersons("contact_prs_sender", "", "'+document.getElementById('id_jur_prs_sender').value+'", "'+document.getElementById('id_service_place_sender').value+'", "40") %>
			</td>			
		</tr>
 		<tr>
			<td colspan="2">&nbsp;</td>
			<td><%= Bean.logisticXML.getfieldTransl("desc_sender", false) %></td><td><textarea name="desc_sender" cols="60" rows="3" class="inputfield"></textarea></td>
		</tr>
 		<tr>
			<td colspan="6" align="center">
				<%=Bean.getSubmitButtonAjax("../crm/logistic/partners/samupdate.jsp") %>
				<%=Bean.getResetButton() %>
				<% if ("add".equalsIgnoreCase(action)) { %>
					<%=Bean.getGoBackButton("../crm/logistic/partners/sams.jsp") %>
				<% } else { %>
					<%=Bean.getGoBackButton("../crm/logistic/partners/samspecs.jsp?id=" + id) %>
				<% } %>
			</td>
		</tr>

	</table>
	
	<!-- Скрипт для втавки меню вибору дати -->
	<%= Bean.getCalendarScript("action_date", false) %>

</form>

	        <%
    	} else {
    	    %> <%= Bean.getUnknownActionText(action) %><%
    	}
	
	} else if (process.equalsIgnoreCase("yes"))	{
    	
		String
    		id_jur_prs_receiver			= Bean.getDecodeParam(parameters.get("id_jur_prs_receiver")),
    		id_service_place_receiver	= Bean.getDecodeParam(parameters.get("id_service_place_receiver")),
    		id_contact_prs_receiver		= Bean.getDecodeParam(parameters.get("id_contact_prs_receiver")),
    		desc_receiver 				= Bean.getDecodeParam(parameters.get("desc_receiver")),
    		id_jur_prs_sender 			= Bean.getDecodeParam(parameters.get("id_jur_prs_sender")),
    		id_service_place_sender 	= Bean.getDecodeParam(parameters.get("id_service_place_sender")),
    		id_contact_prs_sender 		= Bean.getDecodeParam(parameters.get("id_contact_prs_sender")),
    		desc_sender 				= Bean.getDecodeParam(parameters.get("desc_sender")),
    		action_date 				= Bean.getDecodeParam(parameters.get("action_date")),
    		object_count 				= Bean.getDecodeParam(parameters.get("object_count")),
    		operation_desc 				= Bean.getDecodeParam(parameters.get("operation_desc")),
    		id_club		 				= Bean.getDecodeParam(parameters.get("id_club"));

    	if (action.equalsIgnoreCase("add")) { 
    		String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_LG_CARD.add_action(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";

			String[] pParam = new String [14];

			pParam[0] = "SAM";
			pParam[1] = operation_desc;
			pParam[2] = id_jur_prs_receiver;
			pParam[3] = id_service_place_receiver;
			pParam[4] = id_contact_prs_receiver;
			pParam[5] = desc_receiver;
			pParam[6] = id_jur_prs_sender;
			pParam[7] = id_service_place_sender;
			pParam[8] = id_contact_prs_sender;
			pParam[9] = desc_sender;
			pParam[10] = action_date;
			pParam[11] = object_count;
			pParam[12] = id_club;
			pParam[13] = Bean.getDateFormat();
		
		 	%>
			<%= Bean.getCallResultParam("INSERT", callSQL, pParam, "../crm/logistic/partners/samspecs.jsp?id=" , "../crm/logistic/partners/sams.jsp") %>
			<% 	

    	} else if (action.equalsIgnoreCase("remove")) { 
    		
    		String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_LG_CARD.delete_action(?,?)}";

			String[] pParam = new String [1];

			pParam[0] = id;

		 	%>
			<%= Bean.getCallResultParam("DELETE", callSQL, pParam, "../crm/logistic/partners/sams.jsp" , "") %>
			
			<% 	

    	} else if (action.equalsIgnoreCase("edit")) { 
    		
    		String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_LG_CARD.update_action(?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";

			String[] pParam = new String [13];

			pParam[0] = id;
			pParam[1] = operation_desc;
			pParam[2] = id_jur_prs_receiver;
			pParam[3] = id_service_place_receiver;
			pParam[4] = id_contact_prs_receiver;
			pParam[5] = desc_receiver;
			pParam[6] = id_jur_prs_sender;
			pParam[7] = id_service_place_sender;
			pParam[8] = id_contact_prs_sender;
			pParam[9] = desc_sender;
			pParam[10] = action_date;
			pParam[11] = object_count;
			pParam[12] = Bean.getDateFormat();
		
		 	%>
			<%= Bean.getCallResultParam("UPDATE", callSQL, pParam, "../crm/logistic/partners/samspecs.jsp?id=" + id, "") %>
			
			<% 	

    	} else { %> 
    		<%= Bean.getUnknownActionText(action) %><% 
    	}
	} else {
    	%> <%= Bean.getUnknownProcessText(process) %> <%
	}
} else if (type.equalsIgnoreCase("sam")) {
	if (process.equalsIgnoreCase("no"))
		/* вибираємо тип дії (добавити, видалити...)*/
		{
		   /*  --- Добавити запис --- */
	    	if (action.equalsIgnoreCase("add")) {
	    		
		        %>
		<script>
			var formData = new Array (
					new Array ('id_sam', 'varchar2', 1)
			);
		</script> 
			<%= Bean.getOperationTitle(
		    		Bean.logisticXML.getfieldTransl("h_add_sam", false),
					"Y",
					"N") 
			%>
	    <form action="../crm/logistic/partners/samupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return validateForm(formData)">
	        <input type="hidden" name="action" value="add">
	        <input type="hidden" name="process" value="yes">
	    	<input type="hidden" name="type" value="sam">
	    	<input type="hidden" name="id" value="<%=id %>">
		<table <%=Bean.getTableDetailParam() %>>
			<tr>
				<td><%= Bean.samXML.getfieldTransl("id_sam", false) %></td><td><input type="text" name="id_sam" size="40" value="" class="inputfield"></td>
			</tr>
	 		<tr>
				<td colspan="6" align="center">
					<%=Bean.getSubmitButtonAjax("../crm/logistic/partners/samupdate.jsp") %>
					<%=Bean.getResetButton() %>
					<%=Bean.getGoBackButton("../crm/logistic/partners/samspecs.jsp?id="+id) %>
				</td>
			</tr>

		</table>
	</form>

		        <%
	    	} else if (action.equalsIgnoreCase("add_list")) {
	    		
		        %>
			<%= Bean.getOperationTitle(
		    		Bean.logisticXML.getfieldTransl("h_add_sam_select", false),
					"Y",
					"N") 
			%>
	    <%
    	bcLGObject lgSAM = new bcLGObject("SAM", id);
	    
	    String l_sam_page	= Bean.getDecodeParam(parameters.get("sam_page"));
	    Bean.pageCheck(pageFormName + tagSAMEdit, l_sam_page);
	    String l_sam_page_beg = Bean.getFirstRowNumber(pageFormName + tagSAMEdit);
	    String l_sam_page_end = Bean.getLastRowNumber(pageFormName + tagSAMEdit);

    	%>
		<script type="text/javascript">
		 	function CheckCB(Element) {
				myCheck = true;
		
				thisCheckBoxes = document.getElementsByTagName('input');
				for (i = 1; i < thisCheckBoxes.length; i++) { 
					myName = thisCheckBoxes[i].name;
					if (myName.substr(0,7) == 'chb_sam'){
						myCheck = myCheck && thisCheckBoxes[i].checked;
					}
				}
				if (document.getElementById('mainCheck')) {
					document.getElementById('mainCheck').checked = myCheck;
				}
			}
			function CheckAll(Element,Name) {
				thisCheckBoxes = document.getElementsByTagName('input');
				for (i = 1; i < thisCheckBoxes.length; i++) { 
					myName = thisCheckBoxes[i].name;
					
					if (myName.substr(0,7) == Name){
							thisCheckBoxes[i].checked = document.getElementById('mainCheck').checked;
					}
				}
			}
			CheckCB();
		</script>
		<table <%=Bean.getTableMenuParam() %>>
		<tr>
		<td>&nbsp;
		</td>
		<!-- Вывод страниц -->
		<%= Bean.getPagesHTML(pageFormName + tagSAMEdit, "../crm/logistic/partners/samupdate.jsp?id=" + id + "&type=sam&process=no&action=add_list&", "sam_page") %>
		</tr>
		</table>
		<%=lgSAM.getSAMEditListHTML(l_sam_page_beg, l_sam_page_end) %>

		        <%
	    	} else {
	    	    %> <%= Bean.getUnknownActionText(action) %><%
	    	}
		
		} else if (process.equalsIgnoreCase("yes"))	{
	    	
			String
	    		id_sam			= Bean.getDecodeParam(parameters.get("id_sam"));

	    	if (action.equalsIgnoreCase("add")) { 
	    		String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_LG_CARD.add_sam(?,?,?)}";

				String[] pParam = new String [2];

				pParam[0] = id;
				pParam[1] = id_sam;
			
			 	%>
				<%= Bean.getCallResultParam("UPDATE", callSQL, pParam, "../crm/logistic/partners/samspecs.jsp?id=" + id, "") %>
				<% 	

	        } else if (action.equalsIgnoreCase("add_list")) { %> 
	        
				<%= Bean.form_messageXML.getfieldTransl("processing_update", false) %>
			   	<% 
		 
			   	String[] results = new String[2];
			   	
		 		ArrayList<String> id_value=new ArrayList<String>();
				ArrayList<String> prv_value=new ArrayList<String>();
	
		 		String callSQL = "";
		 		Set<String> keySet = parameters.keySet();
	    		Iterator<String> keySetIterator = keySet.iterator();
	    		String key = "";
		    	while(keySetIterator.hasNext()) {
					try{
						key = (String)keySetIterator.next();
						if(key.contains("chb_sam")){
							id_value.add(key.substring(8));
						}
						if(key.contains("prv_sam")){
							prv_value.add(key.substring(8));
						}
					}
					catch(Exception ex){
						Bean.writeException(
								"../crm/logistic/partners/samupdate.jsp",
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
			    
			    String[] pParam = new String [2];
	
			    if (id_value.size()>0) {
			 		 for(int counter=0;counter<id_value.size();counter++){ 
			 			 if (!(prv_value.contains(id_value.get(counter)))) {
			 				 
				        	callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_LG_CARD.add_sam(?,?,?)}";
				        	
				        	pParam[0] = id;
							pParam[1] = id_value.get(counter);
				        		
							results = Bean.myCallFunctionParam(callSQL, pParam, 2);
							resultInt = results[0];
							resultMessage = results[1];
						
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
					   	 				 
				        	callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_LG_CARD.delete_sam(?,?,?)}";
					        	
					        pParam[0] = id;
							pParam[1] = prv_value.get(counter);
					        		
							results = Bean.myCallFunctionParam(callSQL, pParam, 2);
							resultInt = results[0];
							resultMessage = results[1];
						
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
		   	    		"../crm/logistic/partners/samspecs.jsp?id=" + id, 
		   	    		"../crm/logistic/partners/samspecs.jsp?id=" + id, 
		   	    		Bean.form_messageXML.getfieldTransl("save_error", false)) %>
		   		<% 

	    	} else if (action.equalsIgnoreCase("remove")) { 
	    		
	    		String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_LG_CARD.delete_sam(?,?,?)}";
				
				String[] pParam = new String [2];

				pParam[0] = id;
				pParam[1] = line;

			 	%>
				<%= Bean.getCallResultParam("DELETE", callSQL, pParam, "../crm/logistic/partners/samspecs.jsp?id=" + id, "") %>
				
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
