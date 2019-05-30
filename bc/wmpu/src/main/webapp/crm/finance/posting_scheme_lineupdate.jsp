<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcCRMBean"  />

<%@page import="bc.objects.bcFNPostingSchemeLineObject"%>
<%@page import="java.util.Enumeration"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.HashMap"%>
<%@page import="bc.objects.bcClubRelationshipObject"%>
<%@page import="bc.objects.bcClubRelationshipBKOperationShemeObject"%>
<%@page import="bc.objects.bcFNPostingSchemeObject"%>
<%@page import="java.util.Set"%>
<%@page import="java.util.Iterator"%><html>

<%= Bean.getLogOutScript(request) %>

<head>
	<%= Bean.getMetaContent() %>
	<%= Bean.getBottomFrameCSS() %>

</head>


<%
HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());

String pageFormName = "FINANCE_POSTING_SCHEME";

Bean.setJspPageForTabName(pageFormName);

request.setCharacterEncoding("UTF-8");

String id			= Bean.getDecodeParam(parameters.get("id")); 
String id_scheme	= Bean.getDecodeParam(parameters.get("id_scheme"));
String action		= Bean.getDecodeParam(parameters.get("action")); 
String type			= Bean.getDecodeParam(parameters.get("type")); 
String process		= Bean.getDecodeParam(parameters.get("process"));

if (id==null || ("".equalsIgnoreCase(id))) id="";
if (action==null || ("".equalsIgnoreCase(action))) action="empty";
if (type==null || ("".equalsIgnoreCase(type))) type="empty";
if (process==null || ("".equalsIgnoreCase(process))) process="empty";

if (type.equalsIgnoreCase("general")) {
	if (process.equalsIgnoreCase("no"))
/* вибираємо тип дії (добавити, видалити...)*/
	{
    %> 
<body>
	<%	
	   /*  --- Добавити запис --- */
    	if (action.equalsIgnoreCase("add") || action.equalsIgnoreCase("add2")) {
    		
    		bcFNPostingSchemeObject scheme = new bcFNPostingSchemeObject(id_scheme);
    		
	        %> 
			<%= Bean.getOperationTitle(
					Bean.posting_schemeXML.getfieldTransl("h_postings_settings_add", false),
					"Y",
					"Y") 
			%>

        <script>
	        var formAll = new Array ();
			var formData = new Array (
				new Array ('debet_name_bk_account_scheme_line', 'varchar2', 1),
		   		new Array ('credit_name_bk_account_scheme_line', 'varchar2', 1),
				new Array ('oper_number', 'varchar2', 1),
				new Array ('amount', 'varchar2', 1)
			);
			var formDataAddit = new Array (
				new Array ('name_related_bk_oper_scheme_line', 'varchar2', 1)
			);
			var formDataClearing = new Array (
				new Array ('receiver_cd_bk_bank_accnt_type', 'varchar2', 1),
				new Array ('payer_cd_bk_bank_accnt_type', 'varchar2', 1)
			);
			var formExport = new Array (
				new Array ('cd_bk_doc_type', 'varchar2', 1)
			);
			function myValidateForm() {
				var phase = document.getElementById('cd_bk_phase').value;
				var using = document.getElementById('using_in_clearing').value;
				var run_exp = document.getElementById('run_postings_export').value;
	
				formAll = formAll.concat(formData);
				if (phase == 'AFTER_MONEY_TRANSFER') {
					formAll = formAll.concat(formDataAddit);
				}
				if (using == 'Y') {
					formAll = formAll.concat(formDataClearing);
				}
				if (run_exp == 'Y') {
					formAll = formAll.concat(formExport);
				}
				return validateForm(formAll);
			}

    		function checkPhase(){
    			var phase = document.getElementById('cd_bk_phase').value;
    			if (phase == 'AFTER_MONEY_TRANSFER') {
    				document.getElementById('rel_scheme').innerHTML='<%= Bean.posting_schemeXML.getfieldTransl("id_related_bk_oper_scheme_line", true) %>';
    			} else {
    				document.getElementById('rel_scheme').innerHTML='<%= Bean.posting_schemeXML.getfieldTransl("id_related_bk_oper_scheme_line", false) %>';
    			}
    		}

    		function checkBKExport(){
    			var run_exp = document.getElementById('run_postings_export').value;
    			if (run_exp == 'Y') {
    				document.getElementById('span_cd_bk_doc_type').innerHTML='<%= Bean.posting_schemeXML.getfieldTransl("cd_bk_doc_type", true) %>';
    			} else {
    				document.getElementById('span_cd_bk_doc_type').innerHTML='<%= Bean.posting_schemeXML.getfieldTransl("cd_bk_doc_type", false) %>';
    			}
    		}

    		function checkClearing(){
    			var using = document.getElementById('using_in_clearing').value;
    			if (using == 'Y') {
    				document.getElementById('span_receiver_cd_bk_bank_accnt_type').innerHTML='<%= Bean.posting_schemeXML.getfieldTransl("receiver_cd_bk_bank_accnt_type", true) %>';
    				document.getElementById('span_payer_cd_bk_bank_accnt_type').innerHTML='<%= Bean.posting_schemeXML.getfieldTransl("payer_cd_bk_bank_accnt_type", true) %>';
    			} else {
    				document.getElementById('span_receiver_cd_bk_bank_accnt_type').innerHTML='<%= Bean.posting_schemeXML.getfieldTransl("receiver_cd_bk_bank_accnt_type", false) %>';
    				document.getElementById('span_payer_cd_bk_bank_accnt_type').innerHTML='<%= Bean.posting_schemeXML.getfieldTransl("payer_cd_bk_bank_accnt_type", false) %>';
    			}
    		}

    		var debetPart = '<select name=\"debet_participant_type\"  class=\"inputfield\"><%= Bean.getBKOperationTypeAccountParticipantOptions("REMITTANCE_CARD_CARD", "", true) %></select>';
    		var creditPart = '<select name=\"credit_participant_type\"  class=\"inputfield\"><%= Bean.getBKOperationTypeAccountParticipantOptions("REMITTANCE_CARD_CARD", "", true) %></select>';
    		 
    		function setParticipant(bkOperScheme) {
    			var deb = document.getElementById('debet_part');
    			var cred = document.getElementById('credit_part');
    			if (bkOperScheme == 'REMITTANCE_CARD_CARD') {
    				deb.innerHTML = debetPart;
    				cred.innerHTML = creditPart;
    			} else {
    				deb.innerHTML = '';
    				cred.innerHTML = '';
    			}
    		}
    		checkPhase();
			checkClearing(); 
			checkBKExport();
			dwr_get_bk_bank_acc_type(document.getElementById('cd_club_rel_type'),'<%=Bean.getSessionId()%>'); 
	</script>
	<form action="../crm/finance/posting_scheme_lineupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return myValidateForm()">
	    <input type="hidden" name="type" value="general">
	    <input type="hidden" name="action" value="add">
	    <input type="hidden" name="actionprev" value="<%=action %>">
	    <input type="hidden" name="process" value="yes">
	    <input type="hidden" name="id" value="<%= id %>">
	    <input type="hidden" name="id_scheme" value="<%= id_scheme %>">
	
		<table <%=Bean.getTableDetailParam() %>>
        <tr>
			<td><%= Bean.relationshipXML.getfieldTransl("general", false) %></td> <td  colspan="3"><select name="cd_club_rel_type" id="cd_club_rel_type" class="inputfield" onchange="dwr_get_bk_bank_acc_type(this,'<%=Bean.getSessionId()%>')"><%= Bean.getClubRelTypeOptions("", false) %></select></td>
			<td><%= Bean.posting_schemeXML.getfieldTransl("exist_flag_tsl", false) %></td> <td valign=top><select name="exist_flag"  class="inputfield"><%= Bean.getMeaningFromLookupNameOptions("YES_NO", "Y", false) %></select></td>
		</tr>
		<tr>
			<td><%= Bean.posting_schemeXML.getfieldTransl("name_bk_operation_type", false) %></td> 
			<td colspan="5">
				<select name="cd_bk_operation_type" id="cd_bk_operation_type" class="inputfield"><%= Bean.getBKOperationTypeOptions("", false) %></select>
			</td>
		</tr>
		<tr>
			<td><%= Bean.posting_schemeXML.getfieldTransl("cd_bk_phase", false) %></td> <td  colspan="3"><select name="cd_bk_phase" id="cd_bk_phase" onchange="checkPhase();" class="inputfield"><%= Bean.getBKPhaseListOptions("", false) %></select></td>
			<td colspan="2" class="top_line"><b><%= Bean.posting_schemeXML.getfieldTransl("h_additional_param", false) %></b></td>
		</tr>
		<tr>
			<td><%= Bean.posting_schemeXML.getfieldTransl("oper_number", true) %></td> <td  colspan="3"><input type="text" name="oper_number" size="20" value="" class="inputfield"> </td>
			<td><%= Bean.posting_schemeXML.getfieldTransl("id_payment_system", false) %></td> <td  colspan="3"><select name="id_payment_system"  class="inputfield"><%= Bean.getPaymentSystemOptions("", true) %></select></td>
		</tr>
		<tr>
			<td valign=top rowspan="4"><%= Bean.posting_schemeXML.getfieldTransl("oper_content", false) %></td><td valign=top  colspan="3" rowspan="4"><textarea name="oper_content" cols="58" rows="5" class="inputfield"></textarea></td>
			<td><%= Bean.posting_schemeXML.getfieldTransl("card_owner", false) %></td> <td><select name="card_owner"  class="inputfield"><%= Bean.getMeaningFromLookupNameOptions("BK_OPER_CARD_OWNER", "", true) %></select></td>
		</tr>
		<tr>
			<td><%= Bean.posting_schemeXML.getfieldTransl("is_vat_payer", false) %></td> <td><select name="is_vat_payer"  class="inputfield"><%= Bean.getMeaningFromLookupNameOptions("YES_NO", "", true) %></select></td>
		</tr>
		<tr>
			<td>&nbsp;</td> <td>&nbsp;</td>
		</tr>
		<tr>
			<td valign=top class="top_line"><%= Bean.posting_schemeXML.getfieldTransl("run_postings_export", false) %></td> <td valign=top class="top_line"><select name="run_postings_export" id="run_postings_export" class="inputfield" onchange="checkBKExport()"><%= Bean.getMeaningFromLookupNameOptions("YES_NO", "N", false) %></select></td>
		</tr>
		<tr>
			<td><%= Bean.posting_schemeXML.getfieldTransl("debet_name_bk_account", true) %></td> 
			<td  colspan="3">
				<%=Bean.getWindowBKAccountSchemeLine("debet_", "", "", scheme.getValue("ID_BK_ACCOUNT_SCHEME"), "40") %>
			<br><%= Bean.posting_schemeXML.getfieldTransl("h_participant_type", false) %>&nbsp;<span id="debet_part"></span>
			</td> 
			<td valign=top><span id="span_cd_bk_doc_type"><%= Bean.posting_schemeXML.getfieldTransl("cd_bk_doc_type", false) %></span></td> <td valign=top><select name="cd_bk_doc_type"  class="inputfield"><%= Bean.getBKDocTypeOptions("Y", true) %></select></td>
		</tr>
		<tr>
			<td><%= Bean.posting_schemeXML.getfieldTransl("credit_name_bk_account", true) %></td> 
			<td  colspan="3">
				<%=Bean.getWindowBKAccountSchemeLine("credit_", "", "", scheme.getValue("ID_BK_ACCOUNT_SCHEME"), "40") %>
			<br><%= Bean.posting_schemeXML.getfieldTransl("h_participant_type", false) %>&nbsp;<span id="credit_part"></span>
			</td>
			<td class="top_line"><%= Bean.posting_schemeXML.getfieldTransl("zero_amounts_allowed", false) %> </td><td class="top_line"><select name="zero_amounts_allowed" class="inputfield"><%= Bean.getMeaningFromLookupNameOptions("YES_NO", "N", false) %></select></td>
		</tr>
		<tr>
			<td><span id="rel_scheme"></span></td> 
			<td  colspan="3">
				<%=Bean.getWindowFindBKOperationScheme("related_bk_oper_scheme_line", "", "40") %>
			</td>
			<td valign=top>&nbsp;</td> <td valign=top>&nbsp;</td>
		</tr>
		<tr>
			<td valign=top rowspan="4"><%= Bean.posting_schemeXML.getfieldTransl("amount", true) %></td> 
			<td valign=top  colspan="3" rowspan="4">
				<%=Bean.getWindowCalculator("amount", id, id, "", "Y", "N", "50", "5") %>
			</td>
			<td colspan="2" class="top_line"><b><%= Bean.posting_schemeXML.getfieldTransl("h_clearing_param", false) %></b></td>
		</tr>
		<tr>
			<td valign=top><%= Bean.posting_schemeXML.getfieldTransl("using_in_clearing", false) %></td> <td valign=top><select name="using_in_clearing" id="using_in_clearing" onchange="checkClearing();" class="inputfield"><%= Bean.getMeaningFromLookupNameOptions("YES_NO", "N", false) %></select></td>
		</tr>
		<tr>
			<td valign=top><span id="span_receiver_cd_bk_bank_accnt_type"><%= Bean.posting_schemeXML.getfieldTransl("receiver_cd_bk_bank_accnt_type", false) %></span></td> <td valign=top><select name="receiver_cd_bk_bank_accnt_type" id="receiver_cd_bk_bank_accnt_type" class="inputfield"><option value=""></select></td>
		</tr>
		<tr>
			<td valign=top><span id="span_payer_cd_bk_bank_accnt_type"><%= Bean.posting_schemeXML.getfieldTransl("payer_cd_bk_bank_accnt_type", false) %></span></td> <td valign=top><select name="payer_cd_bk_bank_accnt_type" id="payer_cd_bk_bank_accnt_type" class="inputfield"><option value=""></select></td>
		</tr>
		<tr>
			<td valign=top><%= Bean.posting_schemeXML.getfieldTransl("assignment_posting", false) %></td> 
			   <td valign=top  colspan="3">
				<%=Bean.getWindowCalculator("assignment_posting", id, id, "", "N", "Y", "50", "3") %>
			</td>
			<td valign=top><%= Bean.posting_schemeXML.getfieldTransl("payment_function_default", false) %></td> 
			   <td valign=top>
				<%=Bean.getWindowCalculator("payment_function", id, id, "", "N", "Y", "50", "3") %>
			</td>
		</tr>
		<tr>
			<td colspan="10" align="center">
				<%=Bean.getSubmitButtonAjax("../crm/finance/posting_scheme_lineupdate.jsp") %>
				<%=Bean.getResetButton() %>
			<% if (action.equalsIgnoreCase("add")) { %>
				<%=Bean.getGoBackButton("../crm/finance/posting_schemespecs.jsp?id=" + id_scheme) %>
			<% } else { %>
                <%=Bean.getGoBackButton("../crm/finance/posting_scheme_linespecs.jsp?id=" + id) %>
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
%>
<body>
<%
	
	String
		actionprev		 					= Bean.getDecodeParam(parameters.get("actionprev")),
	
		cd_bk_operation_type 				= Bean.getDecodeParam(parameters.get("cd_bk_operation_type")),
		debet_id_bk_account_scheme_line 	= Bean.getDecodeParam(parameters.get("debet_id_bk_account_scheme_line")),
		debet_participant_type	 			= Bean.getDecodeParam(parameters.get("debet_participant_type")),
    	credit_id_bk_account_scheme_line 	= Bean.getDecodeParam(parameters.get("credit_id_bk_account_scheme_line")),
		credit_participant_type	 			= Bean.getDecodeParam(parameters.get("credit_participant_type")),
    
    	oper_number 						= Bean.getDecodeParam(parameters.get("oper_number")),
    	oper_content 						= Bean.getDecodeParam(parameters.get("oper_content")),
    	amount 								= Bean.getDecodeParam(parameters.get("amount")),
    	exist_flag 							= Bean.getDecodeParam(parameters.get("exist_flag")),
    	assignment_posting 					= Bean.getDecodeParam(parameters.get("assignment_posting")),
    	id_payment_system					= Bean.getDecodeParam(parameters.get("id_payment_system")),
    	cd_phase 							= Bean.getDecodeParam(parameters.get("cd_bk_phase")),
    	using_in_clearing 					= Bean.getDecodeParam(parameters.get("using_in_clearing")),
    	zero_amounts_allowed 				= Bean.getDecodeParam(parameters.get("zero_amounts_allowed")),
    	run_postings_export 				= Bean.getDecodeParam(parameters.get("run_postings_export")),
    	cd_bk_doc_type	 					= Bean.getDecodeParam(parameters.get("cd_bk_doc_type")),
    	card_owner        					= Bean.getDecodeParam(parameters.get("card_owner")),
    	is_vat_payer        				= Bean.getDecodeParam(parameters.get("is_vat_payer")),
    	cd_club_rel_type     				= Bean.getDecodeParam(parameters.get("cd_club_rel_type")),
    	cd_bk_condition     				= Bean.getDecodeParam(parameters.get("cd_bk_condition")),
    	
    	id_rel_bk_oper_scheme 				= Bean.getDecodeParam(parameters.get("id_related_bk_oper_scheme_line")),
    	
    	receiver_cd_bk_bank_accnt_type 		= Bean.getDecodeParam(parameters.get("receiver_cd_bk_bank_accnt_type")),
		payer_cd_bk_bank_accnt_type			= Bean.getDecodeParam(parameters.get("payer_cd_bk_bank_accnt_type")),
		payment_function					= Bean.getDecodeParam(parameters.get("payment_function"));
    
%>
<script>
	//window.alert('parameter=<%=parameters.get("amount")%>\n, prepared=<%=Bean.getDecodeParam(parameters.get("amount"))%>');
</script>
<%
    if (action.equalsIgnoreCase("add")) { 
    	
    	String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_BK_OPER.add_bk_oper_scheme_line("+
    		"?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";

		String[] pParam = new String [26];

		pParam[0] = id_scheme;
		pParam[1] = cd_bk_operation_type;
		pParam[2] = cd_phase;
		pParam[3] = debet_id_bk_account_scheme_line;
		pParam[4] = debet_participant_type;
		pParam[5] = credit_id_bk_account_scheme_line;
		pParam[6] = credit_participant_type;
		pParam[7] = cd_bk_condition;
		pParam[8] = oper_number;
		pParam[9] = oper_content;
		pParam[10] = amount;
		pParam[11] = assignment_posting;
		pParam[12] = id_payment_system;
		pParam[13] = exist_flag;
		pParam[14] = using_in_clearing;
		pParam[15] = zero_amounts_allowed;
		pParam[16] = run_postings_export;
		pParam[17] = cd_bk_doc_type;
		pParam[18] = card_owner;
		pParam[19] = is_vat_payer;
		pParam[20] = cd_club_rel_type;
		pParam[21] = id_rel_bk_oper_scheme;
		pParam[22] = receiver_cd_bk_bank_accnt_type;
		pParam[23] = payer_cd_bk_bank_accnt_type;
		pParam[24] = payment_function;
		pParam[25] = Bean.getDateFormat();
		
		%>
		<%= Bean.getCallResultParam("INSERT", callSQL, pParam, "../crm/finance/posting_schemespecs.jsp?id=" + id_scheme + "&id_line=", "../crm/finance/posting_schemespecs.jsp?id=" + id_scheme) %>
		<% 	

    } else if (action.equalsIgnoreCase("remove")) { 
    	
    	String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_BK_OPER.delete_bk_oper_scheme_line(?,?)}";

		String[] pParam = new String [1];

		pParam[0] = id;
	
		%>
		<%= Bean.getCallResultParam("DELETE", callSQL, pParam, "../crm/finance/posting_schemespecs.jsp?id=" + id_scheme, "../crm/finance/posting_scheme_linespecs.jsp?id=" + id) %>
		<% 	

    } else if (action.equalsIgnoreCase("edit")) { 
    	
    	String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_BK_OPER.update_bk_oper_scheme_line("+ 
    		"?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";

		String[] pParam = new String [26];

		pParam[0] = id;
		pParam[1] = cd_bk_operation_type;
		pParam[2] = cd_phase;
		pParam[3] = debet_id_bk_account_scheme_line;
		pParam[4] = debet_participant_type;
		pParam[5] = credit_id_bk_account_scheme_line;
		pParam[6] = credit_participant_type;
		pParam[7] = cd_bk_condition;
		pParam[8] = oper_number;
		pParam[9] = oper_content;
		pParam[10] = amount;
		pParam[11] = assignment_posting;
		pParam[12] = id_payment_system;
		pParam[13] = exist_flag;
		pParam[14] = using_in_clearing;
		pParam[15] = zero_amounts_allowed;
		pParam[16] = run_postings_export;
		pParam[17] = cd_bk_doc_type;
		pParam[18] = card_owner;
		pParam[19] = is_vat_payer;
		pParam[20] = cd_club_rel_type;
		pParam[21] = id_rel_bk_oper_scheme;
		pParam[22] = receiver_cd_bk_bank_accnt_type;
		pParam[23] = payer_cd_bk_bank_accnt_type;
		pParam[24] = payment_function;
		pParam[25] = Bean.getDateFormat();
		
		%>
		<%= Bean.getCallResultParam("UPDATE", callSQL, pParam, "../crm/finance/posting_scheme_linespecs.jsp?id=" + id, "") %>
		<% 	

    } else if (action.equalsIgnoreCase("check")) { %>

		<%= Bean.form_messageXML.getfieldTransl("processing_update", false) %> 
		<%  
		    
		String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_BK_OPER.check_bk_oper_scheme_line(?,?,?,?)}";

		String[] pParam = new String [1];

		pParam[0] = id;
			
		String[] results = new String[4];
		results = Bean.myCallFunctionParam(callSQL, pParam, 4);
 		String resultInt = results[0];
 		String resultMessage = results[1];
 		String idLine = results[2];
 		String idReport = results[3];
	 		
	 	%>
	   	<%=Bean.showCallResult(
	   	   	callSQL, 
	   	   	resultInt, 
	   	 	resultMessage, 
	   	   	"../crm/finance/posting_scheme_linespecs.jsp?id=" + id + "&id_report=" + idReport, 
	   	   	"../crm/finance/posting_scheme_linespecs.jsp?id=" + id, 
	   	   	Bean.form_messageXML.getfieldTransl("save_error", false)) %>
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
   						"../crm/finance/posting_scheme_lineupdate.jsp",
						type,
						process,
						action,
						Bean.commonXML.getfieldTransl("h_get_param_value_error", false)+key+": " + ex.getMessage());
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
		 			 
		        	callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_BK_OPER.set_club_rel_oper_scheme(?,?,?,?)}";
			
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
			   	 			 
			 		callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_BK_OPER.set_club_rel_oper_scheme(?,?,?,?)}";
						
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
	    		"../crm/finance/posting_scheme_linespecs.jsp?id=" + id, 
	    		"../crm/finance/posting_scheme_linespecs.jsp?id=" + id, 
	    		Bean.form_messageXML.getfieldTransl("save_error", false)) %>
			<% 
		
		} else { %>
			<%= Bean.getUnknownActionText(action) %><% 
		} 
	} else {
    	%> <%= Bean.getUnknownProcessText(process) %> <%
	}
} else if (type.equalsIgnoreCase("relationship")) {
	
	String id_club_rel = Bean.getDecodeParam(parameters.get("id_club_rel"));
	if (process.equalsIgnoreCase("no"))
		/* вибираємо тип дії (добавити, видалити...)*/
			{
			   /*  --- Добавити запис --- */
		    	if (action.equalsIgnoreCase("addneeded")) {

		    		bcClubRelationshipObject rel = new bcClubRelationshipObject("NEEDED", id_club_rel);
		    		String rel_type = rel.getValue("CD_CLUB_REL_TYPE");
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

		        <form action="../crm/finance/posting_scheme_lineupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return myValidateForm()">
			        <input type="hidden" name="type" value="relationship">
			        <input type="hidden" name="action" value="add">
			        <input type="hidden" name="process" value="yes">
			        <input type="hidden" name="id" value="<%=id %>">
					<input type="hidden" name="cd_club_rel_type" value="<%= rel_type %>">
				<table <%=Bean.getTableDetailParam() %>>

				<%=rel.getClubRelAddHTML(rel.getValue("ID_CLUB"), rel_type, action, id, Bean.getSysDate(), Bean.getDateFormatTitle()) %>

		 		<tr>
					<td colspan="6" align="center">
						<%=Bean.getSubmitButtonAjax("../crm/finance/posting_scheme_lineupdate.jsp") %>
						<%=Bean.getResetButton() %>
						<% if ("addneeded".equalsIgnoreCase(action)) { %>
							<%=Bean.getGoBackButton("../crm/finance/posting_scheme_linespecs.jsp?rel_kind=NEEDED&id=" + id) %>
						<% } else { %>
							<%=Bean.getGoBackButton("../crm/finance/posting_scheme_lineupdate.jsp?id="+id+"&type=relationship&action=add&process=no") %>
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
			        /*  --- Видалити запис --- */
		    	} else if (action.equalsIgnoreCase("edit")) {

		    		bcClubRelationshipObject rel = new bcClubRelationshipObject("CREATED", id_club_rel);
		    		String rel_type = rel.getValue("CD_CLUB_REL_TYPE");
		    	    %> 
		<script language="JavaScript">
			<%= rel.getClubRelCheckScript(rel_type)%>
		</script>
		<%= Bean.getOperationTitle(
				Bean.relationshipXML.getfieldTransl("h_edit_relationship", false),
				"Y",
				"Y") 
		%>

    <form action="../crm/finance/posting_scheme_lineupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return myValidateForm()">
        <input type="hidden" name="type" value="relationship">
        <input type="hidden" name="action" value="edit">
        <input type="hidden" name="process" value="yes">
        <input type="hidden" name="id" value="<%=id%>">
	<table <%=Bean.getTableDetailParam() %>>

		<%=rel.getClubRelEditHTML(Bean.getDateFormatTitle()) %>

 		<tr>
			<td colspan="6" align="center">
				<%=Bean.getSubmitButtonAjax("../crm/finance/posting_scheme_lineupdate.jsp") %>
				<%=Bean.getResetButton() %>
				<%=Bean.getGoBackButton("../crm/finance/posting_scheme_linespecs.jsp?rel_kind=CREATED&id=" + id) %>
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
			cd_club_rel_type 				= Bean.getDecodeParamPrepare(parameters.get("cd_club_rel_type")),
			date_club_rel 					= Bean.getDecodeParamPrepare(parameters.get("date_club_rel")),
			desc_club_rel 					= Bean.getDecodeParamPrepare(parameters.get("desc_club_rel")),
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
				<%= Bean.getCallResultParam("INSERT", callSQL, pParam, "../crm/finance/posting_scheme_linespecs.jsp?id=" + id + "&id_club_rel=", "") %>
				<% 	

			} else if (action.equalsIgnoreCase("remove")) { 
				
				String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_REL.delete_relationship(?,?)}";

				String[] pParam = new String [1];

				pParam[0] = id_club_rel;

			 	%>
				<%= Bean.getCallResultParam("DELETE", callSQL, pParam, "../crm/finance/posting_scheme_linespecs.jsp?id=" + id, "") %>
				<% 	

			} else if (action.equalsIgnoreCase("edit")) { 
				
				String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_REL.update_relationship(?,?,?,?,?,?,?,?,?,?,?)}";

				String[] pParam = new String [10];

				pParam[0] = id_club_rel;
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
				<%= Bean.getCallResultParam("UPDATE", callSQL, pParam, "/financt/posting_scheme_linespecs.jsp?id=" + id, "") %>
				<% 	

				} else { %> 
					<%= Bean.getUnknownActionText(action) %><% 
				}
			} else {
		    	%> <%= Bean.getUnknownProcessText(process) %> <%
			}
} else if (type.equalsIgnoreCase("rel_bk_oper")) {
	
	String id_rel = Bean.getDecodeParam(parameters.get("id_club_rel_oper_scheme"));
	if (process.equalsIgnoreCase("no")) {
    	if (action.equalsIgnoreCase("edit")) {

	  		bcClubRelationshipBKOperationShemeObject rel = new bcClubRelationshipBKOperationShemeObject(id_rel);
	  		bcFNPostingSchemeLineObject scheme = new bcFNPostingSchemeLineObject(id);
		    	    %> 

		<%= Bean.getOperationTitle(
				Bean.relationshipXML.getfieldTransl("h_edit_relationship", false),
				"N",
				"N") 
		%>

    <form action="../crm/finance/posting_scheme_lineupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return myValidateForm()">
        <input type="hidden" name="type" value="rel_bk_oper">
        <input type="hidden" name="action" value="edit">
        <input type="hidden" name="process" value="yes">
        <input type="hidden" name="id" value="<%=id%>">
        <input type="hidden" name="id_club_rel_oper_scheme" value="<%=id_rel%>">
        <input type="hidden" name="cd_club_rel_type" id="cd_club_rel_type" value="<%=rel.getValue("CD_CLUB_REL_TYPE") %>">
        <input type="hidden" name="cd_bk_operation_type" id="cd_bk_operation_type" value="<%=scheme.getValue("CD_BK_OPERATION_TYPE") %>">
	<table <%=Bean.getTableDetailParam() %>>
        <tr>
			<td><%= Bean.relationshipXML.getfieldTransl("cd_club_rel_type", false) %></td> <td><input type="text" name="name_club_rel_type" size="100" value="<%=rel.getValue("NAME_CLUB_REL_TYPE") %>" readonly="readonly" class="inputfield-ro"> </td>
		</tr>
        <tr>
			<td><%= Bean.relationshipXML.getfieldTransl("date_club_rel", false) %></td> <td><input type="text" name="date_club_rel" size="20" value="<%=rel.getValue("DATE_CLUB_REL_FRMT") %>" readonly="readonly" class="inputfield-ro"> </td>
		</tr>
        <tr>
			<td><%= Bean.relationshipXML.getfieldTransl("name_club_rel_type", false) %></td> <td><input type="text" name="name_club_rel_type" size="100" value="<%=rel.getValue("NAME_CLUB_REL_TYPE") %>" readonly="readonly" class="inputfield-ro"> </td>
		</tr>
        <tr>
			<td><%= Bean.relationshipXML.getfieldTransl("sname_party1_full", false) %></td> <td><input type="text" name="sname_party1_full" size="100" value="<%=rel.getValue("SNAME_PARTY1") %>" readonly="readonly" class="inputfield-ro"> </td>
		</tr>
        <tr>
			<td><%= Bean.relationshipXML.getfieldTransl("sname_party2_full", false) %></td> <td><input type="text" name="sname_party2_full" size="100" value="<%=rel.getValue("SNAME_PARTY2") %>" readonly="readonly" class="inputfield-ro"> </td>
		</tr>
        <tr>
			<td valign=top><%= Bean.relationshipXML.getfieldTransl("payment_function", false) %></td> 
			   <td valign=top>
				<%=Bean.getWindowCalculator("rel_payment_function", id, rel.getValue("ID_CLUB_REL_OPER_SCHEME"), rel.getValue("PAYMENT_FUNCTION"), "N", "Y", "90", "3") %>
			</td>
		</tr>

 		<tr>
			<td colspan="6" align="center">
				<%=Bean.getSubmitButtonAjax("../crm/finance/posting_scheme_lineupdate.jsp") %>
				<%=Bean.getResetButton() %>
				<%=Bean.getGoBackButton("../crm/finance/posting_scheme_linespecs.jsp?rel_kind=CREATED&id=" + id) %>
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
			id_club_rel_oper_scheme 		= Bean.getDecodeParamPrepare(parameters.get("id_club_rel_oper_scheme")),
			payment_function 				= Bean.getDecodeParamPrepare(parameters.get("rel_payment_function"));

			if (action.equalsIgnoreCase("edit")) { 
				
				String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_BK_OPER.update_relationship(?,?,?)}";

				String[] pParam = new String [2];

				pParam[0] = id_club_rel_oper_scheme;
				pParam[1] = payment_function;
				
			 	%>
				<%= Bean.getCallResultParam("UPDATE", callSQL, pParam, "../crm/finance/posting_scheme_linespecs.jsp?id=" + id, "") %>
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
