<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" 
import="java.io.*,java.util.List,javax.servlet.*"%>
<jsp:useBean id="Bean" scope="session" class="bc.bcCRMBean"  />

<%= Bean.getLogOutScript(request) %>

<%@page import="bc.objects.bcDocumentObject"%>
<%@page import="bc.objects.bcClubRelationshipObject"%>
<%@page import="java.util.Enumeration"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.HashMap"%>
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

String pageFormName = "CLUB_RELATIONSHIP";

Bean.setJspPageForTabName(pageFormName);

request.setCharacterEncoding("UTF-8");
String id		= Bean.getDecodeParam(parameters.get("id")); 
String type     = Bean.getDecodeParam(parameters.get("type"));
String action	= Bean.getDecodeParam(parameters.get("action")); 
String process	= Bean.getDecodeParam(parameters.get("process"));

if (id==null || ("".equalsIgnoreCase(id))) id="empty";
if (type==null || ("".equalsIgnoreCase(type))) type="general";
if (action==null || ("".equalsIgnoreCase(action))) action="empty";
if (process==null || ("".equalsIgnoreCase(process))) process="empty";

if (type.equalsIgnoreCase("general")) {
	if (process.equalsIgnoreCase("no"))
/* вибираємо тип дії (добавити, видалити...)*/
	{
	   /*  --- Добавити запис --- */
    	if (action.equalsIgnoreCase("add") || action.equalsIgnoreCase("adddet")) {
	        
	        %>
			<%= Bean.getOperationTitle(
					Bean.relationshipXML.getfieldTransl("h_add_relationship", false),
					"Y",
					"N") 
			%>
		<%
		   	String id_club = Bean.getCurrentClubID();
		%>
		<script>
			<% if (id_club == null || "".equalsIgnoreCase(id_club)) { %>
			var formData = new Array (
				new Array ('name_club', 'varchar2', 1),
				new Array ('cd_club_rel_type', 'varchar2', 1)
			);
			<% } else { %>
			var formData = new Array (
				new Array ('cd_club_rel_type', 'varchar2', 1)
			);
			<% } %>
			function myValidateForm() {
				return validateForm(formData);
			}
		</script>

		<form action="../crm/club/relationshipupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return validateForm(formData)">
        <input type="hidden" name="type" value="general">
	    <input type="hidden" name="action" value="add2">
	    <input type="hidden" name="process" value="no">
		<% if (!(id_club == null || "".equalsIgnoreCase(id_club))) { %>
	    <input type="hidden" name="id_club" value="<%=id_club %>">
		<% } %>
		<table <%=Bean.getTableDetailParam() %>>
		<%
			if (id_club == null || "".equalsIgnoreCase(id_club)) { 
				
				bcClubShortObject club = new bcClubShortObject(Bean.getCurrentClubID());
			%>
			<tr>
				<td><%= Bean.clubXML.getfieldTransl("club", true) %>
					<%= Bean.getGoToClubLink(club.getValue("ID_CLUB")) %>
				</td>
			  	<td>
					<%=Bean.getWindowFindClub("club", club.getValue("ID_CLUB"), club.getValue("SNAME_CLUB"), "25") %>
		  		</td>
			</tr>
			<%}	%>
		<tr>
			<td><%= Bean.relationshipXML.getfieldTransl("cd_club_rel_type", true) %></td> <td><%= Bean.getClubRelTypeRadio("cd_club_rel_type", "") %></td>
			<td colspan="2">&nbsp;</td>
        </tr>
 		<tr>
			<td colspan="2" align="center">
				<%=Bean.getSubmitButtonAjax("../crm/club/relationshipupdate.jsp") %>
				<% if ("adddet".equalsIgnoreCase(action)) { %>
					<%=Bean.getGoBackButton("../crm/club/relationshipspecs.jsp?id=" + id) %>
				<% } else { %>
					<%=Bean.getGoBackButton("../crm/club/relationship.jsp") %>
				<% } %>
			</td>
		</tr>

	</table>
	</form>

	        <%
	        /*  --- Видалити запис --- */
    	} else if (action.equalsIgnoreCase("add2") || action.equalsIgnoreCase("addneeded")) {

	    	String rel_type 	= "";
	    	bcClubRelationshipObject rel = null;
	    	String id_club      = "";
    		if ("addneeded".equalsIgnoreCase(action)) {
    			rel = new bcClubRelationshipObject("NEEDED", id);
    			id_club = rel.getValue("ID_CLUB");
    			rel_type = rel.getValue("CD_CLUB_REL_TYPE");
    		} else {
    			rel = new bcClubRelationshipObject("UNKNOWN", null);
    			id_club = Bean.getDecodeParam(parameters.get("id_club"));
    	    	rel_type 	= Bean.getDecodeParam(parameters.get("cd_club_rel_type"));
    		}
	        
	        %>

		<body>
			
			<%= Bean.getOperationTitle(
					Bean.relationshipXML.getfieldTransl("h_add_relationship", false),
					"Y",
					"Y") 
			%>

		<script language="JavaScript">
			<%= rel.getClubRelCheckScript(rel_type)%>
		</script>

        <form action="../crm/club/relationshipupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return myValidateForm()">
	        <input type="hidden" name="type" value="general">
	        <input type="hidden" name="action" value="add">
	        <input type="hidden" name="process" value="yes">
			<input type="hidden" name="cd_club_rel_type" value="<%= rel_type %>">
		<table <%=Bean.getTableDetailParam() %>>
		
		<%= rel.getClubRelAddHTML(id_club, rel_type, action, "", Bean.getSysDate(), Bean.getDateFormatTitle()) %>

 		<tr>
			<td colspan="6" align="center">
				<%=Bean.getSubmitButtonAjax("../crm/club/relationshipupdate.jsp") %>
				<%=Bean.getResetButton() %>
				<% if ("addneeded".equalsIgnoreCase(action)) { %>
					<%=Bean.getGoBackButton("../crm/club/relationship.jsp") %>
				<% } else { %>
					<%=Bean.getGoBackButton("../crm/club/relationshipupdate.jsp?type=general&action=add&process=no") %>
				<% } %>
			</td>
		</tr>
	</table>

	</form> 
		<script type="text/javascript">
  		Calendar.setup({
			inputField  : "id_date_club_rel",         // ID поля вводу дати
      		ifFormat    : "<%= Bean.getJSPDateFormat()%>",    // формат дати (23.03.2008)
      		button      : "btn_date_club_rel"       // ID кнопки для меню вибору дати
    	});		
		</script>

	        <%
   	} else {
   	    %> <%= Bean.getUnknownActionText(action) %><%
   	}

} else if (process.equalsIgnoreCase("yes"))	{
    
	String
		id_club_rel 					= Bean.getDecodeParam(parameters.get("id_club_rel")),
		cd_club_rel_type 				= Bean.getDecodeParam(parameters.get("cd_club_rel_type")),
		date_club_rel 					= Bean.getDecodeParam(parameters.get("date_club_rel")),
		desc_club_rel 					= Bean.getDecodeParam(parameters.get("desc_club_rel")),
		id_party1 						= Bean.getDecodeParam(parameters.get("id_party1")),
		id_party1_settlem_accnt 		= Bean.getDecodeParam(parameters.get("id_party1_settlem_accnt")),
		id_party1_club_distrib_accnt 	= Bean.getDecodeParam(parameters.get("id_party1_club_distrib_accnt")),
		id_party1_club_bon_accnt		= Bean.getDecodeParam(parameters.get("id_party1_club_bon_accnt")),
		id_party2						= Bean.getDecodeParam(parameters.get("id_party2")),
		id_party2_settlem_accnt 		= Bean.getDecodeParam(parameters.get("id_party2_settlem_accnt")),
		id_club					 		= Bean.getDecodeParam(parameters.get("id_club"));

	if (action.equalsIgnoreCase("add")) { 
		
		String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_REL.add_relationship(" +
			"?,?,?,?,?,?,?,?,?,?,?,?,?)}";

		String[] pParam = new String [11];
				
		pParam[0] = cd_club_rel_type;
		pParam[1] = date_club_rel;
		pParam[2] = desc_club_rel;
		pParam[3] = id_party1;
		pParam[4] = id_party1_settlem_accnt;
		pParam[5] = id_party1_club_distrib_accnt;
		pParam[6] = id_party1_club_bon_accnt;
		pParam[7] = id_party2;
		pParam[8] = id_party2_settlem_accnt;
		pParam[9] = id_club;
		pParam[10] = Bean.getDateFormat();
		
	 	%>
		<%= Bean.getCallResultParam("INSERT", callSQL, pParam, "../crm/club/relationshipspecs.jsp?id=", "../crm/club/relationship.jsp") %>
		<% 	

	} else if (action.equalsIgnoreCase("remove")) { 
		
		String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_REL.delete_relationship(?,?)}";

		String[] pParam = new String [4];
				
		pParam[0] = id;

	 	%>
		<%= Bean.getCallResultParam("DELETE", callSQL, pParam, "../crm/club/relationship.jsp", "") %>
		<% 	

	} else if (action.equalsIgnoreCase("edit")) { 
		
		String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_REL.update_relationship(" + 
			"?,?,?,?,?,?,?,?,?,?,?)}";

		String[] pParam = new String [10];
					
		pParam[0] = id;
		pParam[1] = date_club_rel;
		pParam[2] = desc_club_rel;
		pParam[3] = id_party1;
		pParam[4] = id_party1_settlem_accnt;
		pParam[5] = id_party1_club_distrib_accnt;
		pParam[6] = id_party1_club_bon_accnt;
		pParam[7] = id_party2;
		pParam[8] = id_party2_settlem_accnt;
		pParam[9] = Bean.getDateFormat();
		
	 	%>
		<%= Bean.getCallResultParam("UPDATE", callSQL, pParam, "../crm/club/relationshipspecs.jsp?id=" + id, "") %>
		<% 	

    } else if (action.equalsIgnoreCase("set_club_rel")) {%> 
    
		<%= Bean.form_messageXML.getfieldTransl("processing_update", false) %>
	   	<% 

		String[] results = new String[2];
	
		ArrayList<String> id_value=new ArrayList<String>();
		ArrayList<String> prv_id_value=new ArrayList<String>();
	
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
					prv_id_value.add(key.substring(7));
				}
			}
			catch(Exception ex){
				Bean.writeException(
						"../crm/club/relationshipupdate.jsp",
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
	    
	    String idClubRel = "";
	    String idBKOperationScheme = "";
	    int _position = 0;
	    
	    String[] pParam = new String [3];
	
	if (id_value.size()>0) {
 		 for(int counter=0;counter<id_value.size();counter++){
 			
 			 if (!(prv_id_value.contains(id_value.get(counter)))) {
 				 
 				_position = id_value.get(counter).indexOf("_");
 				idClubRel = id_value.get(counter).substring(0, _position);
 				idBKOperationScheme = id_value.get(counter).substring(_position+1);
 				
	        	callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_REL.set_club_rel_oper_scheme(?,?,?,?)}";
	        		
	        	pParam[0] = idClubRel;
	        	pParam[1] = idBKOperationScheme;
	        	pParam[2] = "Y";
		
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
	

	 if (prv_id_value.size()>0) {
	 		for(int counter=0;counter<prv_id_value.size();counter++){ 
		 	if (!(id_value.contains(prv_id_value.get(counter)))) {
		 		
 				_position = prv_id_value.get(counter).indexOf("_");
 				idClubRel = prv_id_value.get(counter).substring(0, _position);
 				idBKOperationScheme = prv_id_value.get(counter).substring(_position+1);
		   	 			 
		 		callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_REL.set_club_rel_oper_scheme(?,?,?,?)}";
        		
	        	pParam[0] = idClubRel;
	        	pParam[1] = idBKOperationScheme;
	        	pParam[2] = "N";
		
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
    		"../crm/club/relationshipspecs.jsp?id=" + id, 
    		"../crm/club/relationshipspecs.jsp?id=" + id, 
    		Bean.form_messageXML.getfieldTransl("save_error", false)) %>
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
