<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcCRMBean"  />

<%= Bean.getLogOutScript(request) %>

<%@page import="java.util.Enumeration"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Set"%>
<%@page import="java.util.Iterator"%>
<%@page import="bc.objects.bcClubShortObject"%>
<%@page import="bc.objects.bcQuestionnaireObject"%>
<%@page import="bc.objects.bcPostingDetailObject"%><html>
<head>
	<%= Bean.getMetaContent() %>
	<%= Bean.getBottomFrameCSS() %>
</head>

<%
HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());

String pageFormName = "CARDS_QUESTIONNAIRE_PACK";

Bean.setJspPageForTabName(pageFormName);

request.setCharacterEncoding("UTF-8");

String type		= Bean.getDecodeParam(parameters.get("type"));
String action		= Bean.getDecodeParam(parameters.get("action")); 
String process		= Bean.getDecodeParam(parameters.get("process"));
String id			= Bean.getDecodeParam(parameters.get("id"));

if (id==null || ("".equalsIgnoreCase(id))) id="empty";
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
	<body topmargin="0">

		<script>
			var formData = new Array (
				new Array ('date_reception_pack', 'varchar2', 1),
				new Array ('name_club', 'varchar2', 1),
				new Array ('expected_quest_quantity', 'varchar2', 1),
				new Array ('name_jur_prs_who_has_sold_card', 'varchar2', 1),
				new Array ('name_jur_prs_where_card_sold', 'varchar2', 1)
			);
			function myValidateForm() {
				return validateForm(formData);
			}
			function setServicePlaceVisibility (pValue) {
				document.getElementById('service_place_td').style.visibility = pValue;
			}
		</script>
			<%= Bean.getOperationTitle(
				Bean.questionnaireXML.getfieldTransl("h_pack_add", false),
				"Y",
				"N") 
			%>
	        <form action="../crm/cards/questionnaire_packupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return validateForm(formData)">
	        <input type="hidden" name="action" value="add">
	        <input type="hidden" name="process" value="yes">
	        <input type="hidden" name="type" value="general">
		<table <%=Bean.getTableDetailParam() %>>
			<tr>
			    <td><%= Bean.questionnaireXML.getfieldTransl("date_reception_pack", true) %></td><td><%=Bean.getCalendarInputField("date_reception_pack", "", "10") %></td>
				<td><%= Bean.clubXML.getfieldTransl("club", true) %>
					<%= Bean.getGoToClubLink(club.getValue("ID_CLUB")) %>
			  	</td>
			  	<td>
					<%=Bean.getWindowFindClub("club", club.getValue("ID_CLUB"), club.getValue("SNAME_CLUB"), "25") %>
			  	</td>
			</tr>
			<tr>
				<td><%= Bean.questionnaireXML.getfieldTransl("expected_quest_quantity", true) %></td><td><input type="text" name="expected_quest_quantity" size="15" value="" class="inputfield"></td>
	 		    <td><%= Bean.club_actionXML.getfieldTransl("name_club_event", false) %></td>
			  	<td colspan="2">
					<%=Bean.getWindowFindClubAction("club_event", "", "", "50") %>
		  		</td>
			</tr>
			<tr>
				<td colspan="2">&nbsp;</td>
				<td><%= Bean.questionnaireXML.getfieldTransl("sname_jur_pr_who_has_sold_card", true) %></td>
				<td colspan="2">
					<%=Bean.getWindowFindJurPrs("jur_prs_who_has_sold_card", "", "ALL", "50") %>
				</td>			
			</tr>
			<tr>
				<td colspan="2">&nbsp;</td>
				<td><%= Bean.questionnaireXML.getfieldTransl("sname_jur_prs_where_card_sold", true) %></td>
				<td colspan="2">
					<%=Bean.getWindowFindJurPrs("jur_prs_where_card_sold", "", "ALL", "50") %>
				</td>			
			</tr>
			<tr>
				<td colspan="2">&nbsp;</td>
				<td><input type="radio" name="service_place_type" id="service_place_type_single" value="SINGLE" CHECKED onchange="setServicePlaceVisibility('visible');"> 
					<label class="checbox_label" for="service_place_type_single"><%= Bean.questionnaireXML.getfieldTransl("one_serv_plce_where_card_sold", false) %></label></td>
				<td>
					<span id="service_place_td"><%=Bean.getWindowFindServicePlace("service_place", "", "", "'+document.getElementById('id_jur_prs_where_card_sold').value+'", "", "50") %></span>
				</td>			
			</tr>
			<tr>
				<td colspan="2">&nbsp;</td>
				<td colspan="2">
					<input type="radio" name="service_place_type" id="service_place_type_all" value="ALL" onchange="setServicePlaceVisibility('hidden');"> 
					<label class="checbox_label" for="service_place_type_all"><%= Bean.questionnaireXML.getfieldTransl("all_serv_plce_where_card_sold", false) %></label> 
                </td>
			</tr>
			<tr>
				<td colspan="6" align="center">
					<%=Bean.getSubmitButtonAjax("../crm/cards/questionnaire_packupdate.jsp") %>
					<%=Bean.getResetButton() %>
				<% if (action.equalsIgnoreCase("add2")) { %>
					<%=Bean.getGoBackButton("../crm/cards/questionnaire_packspecs.jsp?id="+id) %>
				<% } else { %>
					<%=Bean.getGoBackButton("../crm/cards/questionnaire_pack.jsp") %>
				<% } %>
					
				</td>
			</tr>
		</table>
		</form>
		<!-- Скрипт для втавки меню вибору дати -->
		<%= Bean.getCalendarScript("date_reception_pack", false) %>

			<% } else {
		    	    %> <%= Bean.getUnknownActionText(action) %><%
		    	}
	} else if (process.equalsIgnoreCase("yes")) {
		    
		String
			id_club							= Bean.getDecodeParam(parameters.get("id_club")),
			date_reception_pack				= Bean.getDecodeParam(parameters.get("date_reception_pack")),
			id_jur_prs_who_has_sold_card	= Bean.getDecodeParam(parameters.get("id_jur_prs_who_has_sold_card")),
			id_jur_prs_where_card_sold		= Bean.getDecodeParam(parameters.get("id_jur_prs_where_card_sold")),
			id_serv_place_where_card_sold	= Bean.getDecodeParam(parameters.get("id_service_place")),
			state_pack						= Bean.getDecodeParam(parameters.get("state_pack")),
			service_place_type				= Bean.getDecodeParam(parameters.get("service_place_type")),
			expected_quest_quantity			= Bean.getDecodeParam(parameters.get("expected_quest_quantity")),
			note_quest_pack					= Bean.getDecodeParam(parameters.get("note_quest_pack")),
			id_club_event					= Bean.getDecodeParam(parameters.get("id_club_event"));
	    
	    if (action.equalsIgnoreCase("add")) { 
			
	    	String pHyperLink = "";
	    	if ("ALL".equalsIgnoreCase(service_place_type)) {
	    		pHyperLink = "../crm/cards/questionnaire_pack.jsp?type=PACK";
	    	} else {
	    		pHyperLink = "../crm/cards/questionnaire_packspecs.jsp?type=PACK&id=";
	    	}
	    	
			String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_QUEST.add_pack("+
				"?,?,?,?,?,?,?,?,?,?,?)}";

			String[] pParam = new String [9];
					
			pParam[0] = date_reception_pack;
			pParam[1] = id_jur_prs_who_has_sold_card;
			pParam[2] = id_jur_prs_where_card_sold;
			pParam[3] = service_place_type;
			pParam[4] = id_serv_place_where_card_sold;
			pParam[5] = expected_quest_quantity;
			pParam[6] = id_club;
			pParam[7] = id_club_event;
			pParam[8] = Bean.getDateFormat();
			
		 	%>
			<%= Bean.getCallResultParam("INSERT", callSQL, pParam, pHyperLink , "../crm/cards/questionnaire_pack.jsp?type=PACK") %>
			<% 	
	    } else if (action.equalsIgnoreCase("remove")) { 
	    	
	    	String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_QUEST.delete_pack(?,?)}";

	    	String[] pParam = new String [1];
	    			
	    	pParam[0] = id;
				
	 		%>
			<%= Bean.getCallResultParam("DELETE", callSQL, pParam, "../crm/cards/questionnaire_pack.jsp?type=PACK" , "") %>
			<% 	
	 	
	    } else if (action.equalsIgnoreCase("edit")) { 
			
			String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_QUEST.update_pack("+
				"?,?,?,?,?,?,?,?,?,?,?)}";

			String[] pParam = new String [10];
						
			pParam[0] = id;
			pParam[1] = date_reception_pack;
			pParam[2] = id_jur_prs_who_has_sold_card;
			pParam[3] = id_jur_prs_where_card_sold;
			pParam[4] = id_serv_place_where_card_sold;
			pParam[5] = state_pack;
			pParam[6] = expected_quest_quantity;
			pParam[7] = note_quest_pack;
			pParam[8] = id_club_event;
			pParam[9] = Bean.getDateFormat();
			
	 		%>
			<%= Bean.getCallResultParam("UPDATE", callSQL, pParam, "../crm/cards/questionnaire_packspecs.jsp?type=PACK&id=" + id, "") %>
			<% 		
	 	
	    } else if (action.equalsIgnoreCase("set_pack")) { %> 
     
		<%= Bean.form_messageXML.getfieldTransl("processing_update", false) %>
	   	<% 
 
	    String[] results = new String[2];
	   	
 		ArrayList<String> id_quest=new ArrayList<String>();

 		String callSQL = "";
 		Set<String> keySet = parameters.keySet();
		Iterator<String> keySetIterator = keySet.iterator();
		String key = "";
		while(keySetIterator.hasNext()) {
			try{
				key = (String)keySetIterator.next();
   				if(key.contains("id_quest")){
   					id_quest.add(key.substring(9));
   				}
			}
			catch(Exception ex){
   				Bean.writeException(
   						"../crm/cards/questionnaire_packupdate.jsp",
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
	    
	    String idTemp = Bean.getDecodeParam(parameters.get("id_quest_pack"));
	    if (idTemp==null || "".equalsIgnoreCase(idTemp) || "-1".equalsIgnoreCase(idTemp)) {
	    	idTemp = "NULL";
	    }
	    

	    if (id_quest.size()>0) {
	 		 for(int counter=0;counter<id_quest.size();counter++){ 

	 			callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_QUEST.set_pack(?,?,?)}";
		        
		        pParam[0] = idTemp;
		    	pParam[1] = id_quest.get(counter);
		        	
		        	%>
		        	<%= Bean.showCallSQL(callSQL) %>
		        	<%
		        		
					results = Bean.myCallFunctionParam(callSQL, pParam, 2);
					resultInt = results[0];
					resultMessage = results[1];
				
					if (!("0".equalsIgnoreCase(resultInt))) {
						resultFull = resultInt;
						resultMessageFull = resultMessageFull + "; " +resultMessage + "<br>";
					}
			}
		}

		
		%>
  	    <%=Bean.showCallResult(
   	    		callSQL, 
   	    		resultFull, 
   	    		resultMessageFull, 
   	    		"../crm/cards/questionnaire_packspecs.jsp?type=PACK&id=" + id, 
   	    		"../crm/cards/questionnaire_packspecs.jsp?type=PACK&id=" + id, 
   	    		Bean.form_messageXML.getfieldTransl("save_error", false)) %>
   		<% 
	 	
	    } else if (action.equalsIgnoreCase("import")) {  
			
			String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_QUEST.import_pack(?,?,?)}";

			String[] pParam = new String [1];
				
			pParam[0] = id;
	
			%>
			<%= Bean.getCallResultParam("RUN", callSQL, pParam, "../crm/cards/questionnaire_packspecs.jsp?type=PACK&id=" + id +"&id_report=", "../crm/cards/questionnaire_pack.jsp?type=PACK") %>
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
