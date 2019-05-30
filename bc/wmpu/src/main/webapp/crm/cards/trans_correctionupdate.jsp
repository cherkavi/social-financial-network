<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcCRMBean"  />

<%= Bean.getLogOutScript(request) %>

<%@page import="java.util.HashMap"%><html>
<head>
	<%= Bean.getMetaContent() %>
	<%= Bean.getBottomFrameCSS() %>

<%
HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());

String pageFormName = "CARDS_TRANSACTIONS";

Bean.setJspPageForTabName(pageFormName);

request.setCharacterEncoding("UTF-8");
 
String action		= Bean.getDecodeParam(parameters.get("action"));
String process		= Bean.getDecodeParam(parameters.get("process"));
String id_trans		= Bean.getDecodeParam(parameters.get("id_trans"));
String err_type		= Bean.getDecodeParam(parameters.get("err_type"));

if (id_trans==null || ("".equalsIgnoreCase(id_trans))) id_trans="";
if (action==null || ("".equalsIgnoreCase(action))) action="empty";
if (process==null || ("".equalsIgnoreCase(process))) process="empty";


String reportId = "";
%>
</head>

<%
if (process.equalsIgnoreCase("no")) {
	if (action.equalsIgnoreCase("add") || action.equalsIgnoreCase("add2")) {
        
        %>
<body topmargin="0">

		<%= Bean.getOperationTitle(
			Bean.transactionXML.getfieldTransl("h_add_transaction", false),
			"Y",
			"N") 
		%>
    <form action="../crm/cards/trans_correctionupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST">
        <input type="hidden" name="action" value="addaddit">
        <input type="hidden" name="process" value="no">
        <input type="hidden" name="id_trans" value="<%=id_trans %>">
	<table <%=Bean.getTableDetailParam() %>>
		<tr>
			<td><%=Bean.transactionXML.getfieldTransl("input_transaction_type", true)%></td>
			<td><select name="input_transaction_type" class="inputfield">
					<option value="SOURCE"><%=Bean.transactionXML.getfieldTransl("input_transaction_source", false)%>
					<option value="PARSED"><%=Bean.transactionXML.getfieldTransl("input_transaction_parsed", false)%>
				</select>
			</td>
		</tr>
 		<tr>
			<td colspan="2" align="center">
				<%=Bean.getSubmitButtonAjax("../crm/cards/trans_correctionupdate.jsp") %>
				<%=Bean.getResetButton() %>
				<% if (action.equalsIgnoreCase("add2")) { %>
					<%=Bean.getGoBackButton("../crm/cards/trans_correctionspecs.jsp?id="+id_trans) %>
				<% } else { %>
 					<%=Bean.getGoBackButton("../crm/cards/trans_correction.jsp") %>
				<% } %>
			</td>
		</tr>

	</table>
	</form>

        <%
        /*  --- Видалити запис --- */
	} else if (action.equalsIgnoreCase("addaddit")) {
        
		String input_transaction_type 	= Bean.getDecodeParam(parameters.get("input_transaction_type")); 
        %>
<body topmargin="0">
	<% if ("PARSED".equalsIgnoreCase(input_transaction_type)) { %>
	<script>
		var formAll = new Array (
		);
		var formTransData = new Array (
			new Array ('card_nr', 'varchar2', 1),
			new Array ('type_trans', 'varchar2', 1),
			new Array ('id_term', 'varchar2', 1),
			new Array ('cd_issuer', 'varchar2', 1),
			new Array ('cd_paym_sys', 'varchar2', 1),
			new Array ('sys_date', 'varchar2', 1),
			new Array ('state_trans', 'varchar2', 1),
			new Array ('id_sam', 'varchar2', 1),
			new Array ('sys_time', 'varchar2', 1),
			new Array ('ver_trans', 'varchar2', 1),
			new Array ('nt_sam', 'varchar2', 1),
			new Array ('curr_pda', 'varchar2', 1),
			new Array ('vk_enc', 'varchar2', 1),
			new Array ('nt_icc', 'varchar2', 1),
			new Array ('err_tx', 'varchar2', 1),
			new Array ('bases_for_changes', 'varchar2', 1),
			new Array ('name_club', 'varchar2', 1)
		);
		var formRecPayment_v0 = new Array (
			new Array ('nt_ext', 'varchar2', 1),
			new Array ('opr_sum', 'varchar2', 1),
			new Array ('sum_bon', 'varchar2', 1),
			new Array ('sum_disc', 'varchar2', 1)
		);
		var formRecPayment_v1_2 = new Array (
			new Array ('clubcard', 'varchar2', 1),
			new Array ('nt_ext', 'varchar2', 1),
			new Array ('opr_sum', 'varchar2', 1),
			new Array ('sum_pay_cash', 'varchar2', 1),
			new Array ('sum_pay_card', 'varchar2', 1),
			new Array ('sum_pay_bon', 'varchar2', 1),
			new Array ('fl_ext_loyl', 'varchar2', 1),
			new Array ('club_sum', 'varchar2', 1),
			new Array ('sum_bon', 'varchar2', 1),
			new Array ('sum_disc', 'varchar2', 1),
			new Array ('cash_card_nr', 'varchar2', 1),
			new Array ('c_nr', 'varchar2', 1),
			new Array ('c_check_nr', 'varchar2', 1)
		);
		var formRecMovBon = new Array (
			new Array ('bal_acc', 'varchar2', 1),
			new Array ('bal_cur', 'varchar2', 1),
			new Array ('bal_bon_per', 'varchar2', 1),
			new Array ('bal_disc_per', 'varchar2', 1)
		);
		var formRecChkCard = new Array (
			new Array ('p_action_chk', 'varchar2', 1),
			new Array ('club_st', 'varchar2', 1),
			new Array ('club_disc', 'varchar2', 1),
			new Array ('club_bon', 'varchar2', 1),
			new Array ('date_acc', 'varchar2', 1),
			new Array ('date_mov', 'varchar2', 1),
			new Array ('bal_acc', 'varchar2', 1),
			new Array ('bal_cur', 'varchar2', 1),
			new Array ('bal_bon_per', 'varchar2', 1),
			new Array ('bal_disc_per', 'varchar2', 1)
		);
		var formRecInvalCard = new Array (
			new Array ('p_action_inval', 'varchar2', 1),
			new Array ('club_st', 'varchar2', 1)
		);
		var formRecStornoBon_v0 = new Array (
			new Array ('nt_ext', 'varchar2', 1),
			new Array ('bal_acc', 'varchar2', 1),
			new Array ('bal_cur', 'varchar2', 1),
			new Array ('bal_bon_per', 'varchar2', 1),
			new Array ('bal_disc_per', 'varchar2', 1)
		);
		var formRecStornoBon_v1_2 = new Array (
			new Array ('nt_ext', 'varchar2', 1),
			new Array ('opr_sum', 'varchar2', 1),
			new Array ('sum_pay_cash', 'varchar2', 1),
			new Array ('sum_pay_card', 'varchar2', 1),
			new Array ('sum_pay_bon', 'varchar2', 1),
			new Array ('sum_bon', 'varchar2', 1),
			new Array ('sum_disc', 'varchar2', 1),
			new Array ('bal_acc', 'varchar2', 1),
			new Array ('bal_cur', 'varchar2', 1),
			new Array ('bal_bon_per', 'varchar2', 1),
			new Array ('bal_disc_per', 'varchar2', 1)
		);
		var formRecPaymentIm = new Array (
			new Array ('nt_ext', 'varchar2', 1),
			new Array ('opr_sum', 'varchar2', 1),
			new Array ('sum_pay_cash', 'varchar2', 1),
			new Array ('sum_pay_card', 'varchar2', 1),
			new Array ('sum_pay_bon', 'varchar2', 1),
			new Array ('club_sum', 'varchar2', 1),
			new Array ('sum_bon', 'varchar2', 1),
			new Array ('sum_disc', 'varchar2', 1),
			new Array ('imid', 'varchar2', 1),
			new Array ('specid', 'varchar2', 1),
			new Array ('clubcard', 'varchar2', 1),
			new Array ('cardid', 'varchar2', 1),
			new Array ('fl_ext_loyl', 'varchar2', 1),
			new Array ('cash_card_nr', 'varchar2', 1),
			new Array ('c_nr', 'varchar2', 1),
			new Array ('c_check_nr', 'varchar2', 1)
		);
		var formRecPaymentExt = new Array (
			new Array ('clubcard', 'varchar2', 1),
			new Array ('nt_ext', 'varchar2', 1),
			new Array ('opr_sum', 'varchar2', 1),
			new Array ('sum_pay_cash', 'varchar2', 1),
			new Array ('sum_pay_card', 'varchar2', 1),
			new Array ('sum_pay_bon', 'varchar2', 1),
			new Array ('fl_ext_loyl', 'varchar2', 1),
			new Array ('club_sum', 'varchar2', 1),
			new Array ('sum_bon', 'varchar2', 1),
			new Array ('sum_disc', 'varchar2', 1),
			new Array ('cash_card_nr', 'varchar2', 1),
			new Array ('c_nr', 'varchar2', 1),
			new Array ('c_check_nr', 'varchar2', 1)
		);
		
		function myValidateForm(){
			var transType = document.getElementById('type_trans').value;
			var verTrans = document.getElementById('ver_trans').value;

			formAll = formTransData;
			
			if (transType == '1') {
				if (verTrans == '0') {
					formAll = formAll.concat(formRecPayment_v0);
				} else {
					formAll = formAll.concat(formRecPayment_v1_2);
				}
			}
			if (transType == '2') {
				formAll = formAll.concat(formRecMovBon);
			}
			if (transType == '3') {
				formAll = formAll.concat(formRecChkCard);
			}
			if (transType == '4') {
				formAll = formAll.concat(formRecInvalCard);
			}
			if (transType == '5') {
				if (verTrans == '0') {
					formAll = formAll.concat(formRecStornoBon_v0);
				} else {
					formAll = formAll.concat(formRecStornoBon_v1_2);
				}
			}
			if (transType == '6') {
				if (verTrans == '0') {
					return false;
				} else {
					formAll = formAll.concat(formRecPaymentIm);
				}
			}
			if (transType == '7') {
				if (verTrans == '0' || verTrans == '1') {
					return false;
				} else {
					formAll = formAll.concat(formRecPaymentExt);
				}
			}
			//alert(formAll);
			return validateForm(formAll);
		}	

		var elements = new Array();
		var titles = new Array();
		var curI = 0;
		var firstText = '<%=Bean.transactionXML.getFirstObligatoryText()%>';
		var lastText = '<%=Bean.transactionXML.getLastObligatoryText()%>';
		
		function disable(element_name){
			//window.alert('disable: '+element_name);
			var element = document.getElementById(element_name);
			var span_elem = document.getElementById('span_'+element_name);
			
			var found = 0;
			
			for (i in elements) {
				if (elements[i] == element.name) {
					found = 1;
					span_elem.innerHTML = titles[i];
				}
			}
			if (found == 0) {
				curI = curI + 1;
				elements[curI] = element.name;
				titles[curI] = span_elem.innerHTML;
				span_elem.innerHTML = titles[curI];
			}
	        
			element.value = '';
			element.className = 'inputfield-ro';
			element.readOnly = 1;
			element.disabled = 1;
			
		}
		function enable(element_name){
			//window.alert('enable: '+element_name);
			var element = document.getElementById(element_name);
			var span_elem = document.getElementById('span_'+element_name);
			
			var found = 0;
			
			for (i in elements) {
				if (elements[i] == element.name) {
					found = 1;
					span_elem.innerHTML = firstText + titles[i] + lastText;
				}
			}
			if (found == 0) {
				curI = curI + 1;
				elements[curI] = element.name;
				titles[curI] = span_elem.innerHTML;
				span_elem.innerHTML = firstText + titles[curI] + lastText;
			}
			element.className = 'inputfield';
			element.readOnly = 0;
			element.disabled = 0;
		}

		function setRecPayment(ver_trans){
			if (ver_trans==0) {
				disable('clubcard');
			} else {
				enable('clubcard');
			}
			enable('nt_ext');
			enable('opr_sum');
			if (ver_trans==0) {
				disable('sum_pay_cash');
				disable('sum_pay_card');
				disable('sum_pay_bon');
				disable('club_sum');
			} else {
				enable('sum_pay_cash');
				enable('sum_pay_card');
				enable('sum_pay_bon');
				enable('club_sum');
			}
			enable('sum_bon');
			enable('sum_disc');
			disable('club_st');
			disable('club_disc');
			disable('club_bon');
			disable('bal_acc');
			disable('bal_bon_per');
			disable('bal_disc_per');
			disable('bal_cur');
			disable('date_acc');
			disable('date_mov');
			if (ver_trans==0) {
				disable('fl_ext_loyl');
				disable('cash_card_nr');
				disable('c_nr');
				disable('c_check_nr');
			} else {
				enable('fl_ext_loyl');
				enable('cash_card_nr');
				enable('c_nr');
				enable('c_check_nr');
			}
			disable('imid');
			disable('specid');
			disable('cardid');
			disable('c_nomenkl');
			document.getElementById('p_action_chk').style.visibility = "hidden";
			document.getElementById('p_action_inval').style.visibility = "hidden";
			document.getElementById('p_action_title_chk').style.visibility = "hidden";
			document.getElementById('p_action_title_inval').style.visibility = "hidden";
			//document.getElementById('p_action').innerHTML='';
		}
		function setRecMovBon(ver_trans){
			disable('nt_ext');
			disable('opr_sum');
			disable('club_sum');
			disable('sum_bon');
			enable('bal_acc');
			disable('sum_pay_cash');
			disable('club_st');
			disable('sum_disc');
			enable('bal_cur');
			disable('sum_pay_card');
			disable('club_disc');
			disable('date_acc');
			enable('bal_bon_per');
			disable('sum_pay_bon');
			disable('club_bon');
			disable('date_mov');
			enable('bal_disc_per');
			disable('fl_ext_loyl');
			disable('cash_card_nr');
			disable('c_nr');
			disable('c_check_nr');
			disable('imid');
			disable('specid');
			disable('clubcard');
			disable('cardid');
			disable('c_nomenkl');
			document.getElementById('p_action_chk').style.visibility = "hidden";
			document.getElementById('p_action_inval').style.visibility = "hidden";
			document.getElementById('p_action_title_chk').style.visibility = "hidden";
			document.getElementById('p_action_title_inval').style.visibility = "hidden";
			//document.getElementById('p_action').innerHTML='';
		}
		function setRecChkCard(ver_trans){
			disable('nt_ext');
			disable('opr_sum');
			disable('club_sum');
			disable('sum_bon');
			enable('bal_acc');
			disable('sum_pay_cash');
			enable('club_st');
			disable('sum_disc');
			enable('bal_cur');
			disable('sum_pay_card');
			enable('club_disc');
			enable('date_acc');
			enable('bal_bon_per');
			disable('sum_pay_bon');
			enable('club_bon');
			enable('date_mov');
			enable('bal_disc_per');
			disable('fl_ext_loyl');
			disable('cash_card_nr');
			disable('c_nr');
			disable('c_check_nr');
			disable('imid');
			disable('specid');
			disable('clubcard');
			disable('cardid');
			disable('c_nomenkl');
			document.getElementById('p_action_chk').style.visibility = "visible";
			document.getElementById('p_action_inval').style.visibility = "hidden";
			document.getElementById('p_action_title_chk').style.visibility = "visible";
			document.getElementById('p_action_title_inval').style.visibility = "hidden";
			//document.getElementById('p_action').outerHTML=rec_chk_card_action;
		}
		function setRecInvalCard(ver_trans){
			disable('nt_ext');
			disable('opr_sum');
			disable('club_sum');
			disable('sum_bon');
			disable('bal_acc');
			disable('sum_pay_cash');
			enable('club_st');
			disable('sum_disc');
			disable('bal_cur');
			disable('sum_pay_card');
			disable('club_disc');
			disable('date_acc');
			disable('bal_bon_per');
			disable('sum_pay_bon');
			disable('club_bon');
			disable('date_mov');
			disable('bal_disc_per');
			disable('fl_ext_loyl');
			disable('cash_card_nr');
			disable('c_nr');
			disable('c_check_nr');
			disable('imid');
			disable('specid');
			disable('clubcard');
			disable('cardid');
			disable('c_nomenkl');
			document.getElementById('p_action_chk').style.visibility = "hidden";
			document.getElementById('p_action_inval').style.visibility = "visible";
			document.getElementById('p_action_title_chk').style.visibility = "hidden";
			document.getElementById('p_action_title_inval').style.visibility = "visible";
			//document.getElementById('p_action').outerHTML=rec_inval_card_action;
		}
		function setRecStornoBon(ver_trans){
			enable('nt_ext');
			enable('opr_sum');
			disable('club_sum');
			enable('sum_bon');
			enable('bal_acc');
			if (ver_trans==1) {
				enable('sum_pay_cash');
			} else {
				disable('sum_pay_cash');
			}
			disable('club_st');
			enable('sum_disc');
			enable('bal_cur');
			enable('sum_pay_card');
			disable('club_disc');
			disable('date_acc');
			enable('bal_bon_per');
			enable('sum_pay_bon');
			disable('club_bon');
			disable('date_mov');
			enable('bal_disc_per');
			disable('fl_ext_loyl');
			disable('cash_card_nr');
			disable('c_nr');
			disable('c_check_nr');
			disable('imid');
			disable('specid');
			disable('clubcard');
			disable('cardid');
			disable('c_nomenkl');
			document.getElementById('p_action_chk').style.visibility = "hidden";
			document.getElementById('p_action_inval').style.visibility = "hidden";
			document.getElementById('p_action_title_chk').style.visibility = "hidden";
			document.getElementById('p_action_title_inval').style.visibility = "hidden";
			//document.getElementById('p_action').innerHTML='';
		}
		function setRecPaymentIm(ver_trans){
			enable('nt_ext');
			enable('opr_sum');
			enable('club_sum');
			enable('sum_bon');
			disable('bal_acc');
			enable('sum_pay_cash');
			disable('club_st');
			enable('sum_disc');
			disable('bal_cur');
			enable('sum_pay_card');
			disable('club_disc');
			disable('date_acc');
			disable('bal_bon_per');
			enable('sum_pay_bon');
			disable('club_bon');
			disable('date_mov');
			disable('bal_disc_per');
			enable('fl_ext_loyl');
			enable('cash_card_nr');
			enable('c_nr');
			enable('c_check_nr');
			enable('imid');
			enable('specid');
			enable('clubcard');
			enable('cardid');
			disable('c_nomenkl');
			document.getElementById('p_action_chk').style.visibility = "hidden";
			document.getElementById('p_action_inval').style.visibility = "hidden";
			document.getElementById('p_action_title_chk').style.visibility = "hidden";
			document.getElementById('p_action_title_inval').style.visibility = "hidden";
			//document.getElementById('p_action').innerHTML='';
		}
		function setRecPaymentExt(ver_trans){
			enable('nt_ext');
			enable('opr_sum');
			enable('club_sum');
			enable('sum_bon');
			disable('bal_acc');
			enable('sum_pay_cash');
			disable('club_st');
			enable('sum_disc');
			disable('bal_cur');
			enable('sum_pay_card');
			disable('club_disc');
			disable('date_acc');
			disable('bal_bon_per');
			enable('sum_pay_bon');
			disable('club_bon');
			disable('date_mov');
			disable('bal_disc_per');
			enable('fl_ext_loyl');
			enable('cash_card_nr');
			enable('c_nr');
			enable('c_check_nr');
			disable('imid');
			disable('specid');
			enable('clubcard');
			disable('cardid');
			enable('c_nomenkl');
			document.getElementById('p_action_chk').style.visibility = "hidden";
			document.getElementById('p_action_inval').style.visibility = "hidden";
			document.getElementById('p_action_title_chk').style.visibility = "hidden";
			document.getElementById('p_action_title_inval').style.visibility = "hidden";
			//document.getElementById('p_action').innerHTML='';
		}
		function checkTransType(){
			var transType = document.getElementById('type_trans').value;
			var verTrans = document.getElementById('ver_trans').value;

			if (!(verTrans=="0" || verTrans=="1" || verTrans=="2" || verTrans=="3" || verTrans=="4")) {
				alertText = '<%= Bean.transactionXML.getfieldTransl("h_this_trans_ver_not_found", false) %>';
				alertText = alertText.replace("%VER_TRANS%", verTrans);
				window.alert(alertText);
				return false;
			}
			
			if (transType == '1') setRecPayment(verTrans);
			if (transType == '2') setRecMovBon(verTrans);
			if (transType == '3') setRecChkCard(verTrans);
			if (transType == '4') setRecInvalCard(verTrans);
			if (transType == '5') setRecStornoBon(verTrans);
			if (transType == '6') {
				if (verTrans=="0" || verTrans=="3" || verTrans=="4") {
					alertText = '<%= Bean.transactionXML.getfieldTransl("h_this_trans_ver_not_found", false) %>';
					alertText = alertText.replace("%VER_TRANS%", verTrans);
					window.alert(alertText);
					return false;
				} else {
					setRecPaymentIm(verTrans);
				}
			}
			if (transType == '7') {
				if (verTrans=="0" || verTrans=="1") {
					alertText = '<%= Bean.transactionXML.getfieldTransl("h_this_trans_ver_not_found", false) %>';
					alertText = alertText.replace("%VER_TRANS%", verTrans);
					window.alert(alertText);
					return false;
				} else {
					setRecPaymentExt(verTrans);
				}
			}
			return true;
		}	
		checkTransType();
		
	</script>
	<% } else if ("SOURCE".equalsIgnoreCase(input_transaction_type)) { %>
		<script>
		var formData = new Array (
			new Array ('type_trans', 'varchar2', 1),
			new Array ('ver_trans', 'varchar2', 1),
			new Array ('is_crypted', 'varchar2', 1),
			new Array ('src_trans', 'varchar2', 1),
			new Array ('name_club', 'varchar2', 1)
		);
		function myValidateForm(){
			return validateForm(formData);
		}
		</script>
	<% } %>

		<%= Bean.getOperationTitle(
			Bean.transactionXML.getfieldTransl("h_add_transaction", false),
			"Y",
			"N") 
		%>
    <form action="../crm/cards/trans_correctionupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return myValidateForm()">
	        <input type="hidden" name="action" value="add">
	        <input type="hidden" name="process" value="yes">
	        <input type="hidden" name="input_transaction_type" value="<%=input_transaction_type %>">
			<input type="hidden" name="entered_manually" id="entered_manually" value="Y">
	<table <%=Bean.getTableDetailParam() %>> 
		<% if ("PARSED".equalsIgnoreCase(input_transaction_type)) { %>
		<tr>
			<% String id_club = Bean.getCurrentClubID(); %>
			<td><%= Bean.clubXML.getfieldTransl("club", true) %>
				<%= Bean.getGoToClubLink(id_club) %>
		  	</td>
		  	<td>
				<%=Bean.getWindowFindClub("club", id_club, "25") %>
		  	</td>
			<td><%= Bean.transactionXML.getfieldTransl("id_term", true) %><td>
				<input type="text" name="id_term" id="id_term" size="20" value="" class="inputfield" title="ID_TERM">
			</td>			
		</tr>
		<tr>
			<td><%= Bean.transactionXML.getfieldTransl("type_trans_txt", true) %></td> <td><select name="type_trans" id="type_trans" class="inputfield" onchange="checkTransType();"><%= Bean.getTransTypeOptions("1", true) %></select></td>
			<td><%= Bean.transactionXML.getfieldTransl("id_sam", true) %><td>
				<input type="text" name="id_sam" id="id_sam" size="20" value="" class="inputfield" title="ID_SAM">
			</td>			
		</tr>
		<tr>
			<td><%= Bean.transactionXML.getfieldTransl("state_trans_tsl", true) %></td> <td><select name="state_trans" id="state_trans" class="inputfield"><%= Bean.getTransStateOptions("0", false) %></select></td>
			<td><%= Bean.transactionXML.getfieldTransl("nt_sam", true) %></td> <td><input type="text" name="nt_sam" id="nt_sam" size="20" value="" class="inputfield" title="NT_SAM"> </td>
		</tr>
		<tr>
			<td><%= Bean.transactionXML.getfieldTransl("sys_date", true) %></td> <td><input type="text" name="sys_date" id="sys_date" size="20" value="" class="inputfield" title="SYS_DATE"> </td>
			<td><span id="span_nt_ext"><%= Bean.transactionXML.getfieldTransl("nt_ext", false) %></span></td><td><input type="text" name="nt_ext" id="nt_ext" size="20" value="0" class="inputfield" title="NT_EXT"></td>
		</tr>
		<tr>
			<td><%= Bean.transactionXML.getfieldTransl("sys_time", true) %></td> <td><input type="text" name="sys_time" id="sys_time" size="20" value="" class="inputfield" title="SYS_TIME"> </td>
			<td valign="top"><span id="span_clubcard"><%= Bean.transactionXML.getfieldTransl("clubcard", false) %></span></td> <td><select name="clubcard" id="clubcard" class="inputfield" title="CLUBCARD"><option value=""></option><%= Bean.getMeaningFromLookupNumberOptions("YES_NO", "N", false) %></select></td>
		</tr>
		<tr>
			<td><%= Bean.transactionXML.getfieldTransl("ver_trans", true) %></td> <td><select name="ver_trans" id="ver_trans" class="inputfield" title="VER_TRANS" onchange="checkTransType();"><%= Bean.getMeaningFromLookupNameOrderByNymberValueOptions("VER_TELGR","1",true) %></select></td>
			<td><%= Bean.transactionXML.getfieldTransl("card_serial_number", true) %></td> 
			<td colspan="3">
				<input type="text" name="card_nr" id="card_nr" size="20" value="" class="inputfield" title="CARD_NR">
			</td>			
		</tr>
		<tr>
			<td><%= Bean.transactionXML.getfieldTransl("vk_enc", true) %></td><td><select name="vk_enc" id="vk_enc" class="inputfield" title="VK_ENC"><%= Bean.getMeaningFromLookupNameOrderByNymberValueOptions("VER_KEY","1",true) %></select></td>
			<td><%= Bean.transactionXML.getfieldTransl("cd_issuer", true) %></td> <td> <input type="text" name="cd_issuer" id="cd_issuer" size="20" value="" class="inputfield" title="ID_ISSUER"> </td>
		</tr>
		<tr>
			<td><%= Bean.transactionXML.getfieldTransl("err_tx", true) %></td> <td><input type="text" name="err_tx" id="err_tx" size="20" value="9000" class="inputfield" title="ERR_TX"> </td>
			<td><%= Bean.transactionXML.getfieldTransl("cd_paym_sys", true) %></td> <td> <input type="text" name="cd_paym_sys" id="cd_paym_sys" size="20" value="" class="inputfield" title="ID_PAYM_SYS"> </td>
		</tr>
		<tr>
			<td><%= Bean.transactionXML.getfieldTransl("mac_icc", true) %></td> <td><input type="text" name="mac_icc" id="mac_icc" size="20" value="" class="inputfield" title="MAC_ICC"> </td>
			<td><%= Bean.transactionXML.getfieldTransl("nt_icc", true) %></td> <td> <input type="text" name="nt_icc" id="nt_icc" size="20" value="" class="inputfield" title="NT_ICC"> </td>
		</tr>
		<tr>
			<td><%= Bean.transactionXML.getfieldTransl("mac_pda", true) %></td> <td><input type="text" name="mac_pda" id="mac_pda" size="20" value="" class="inputfield" title="MAC_PDA"> </td>
			<td><%= Bean.transactionXML.getfieldTransl("cd_currency", true) %></td><td><input type="text" name="curr_pda" id="curr_pda" size="20" value="" class="inputfield" title="CURR_PDA"></td>
		</tr>
		<tr>
			<td><span id="p_action_title_chk"><%= Bean.transactionXML.getfieldTransl("action", true) %></span>
            </td>
			<td>
	            <select name="p_action_chk" id="p_action_chk" class="inputfield">
					<%= Bean.getMeaningFromLookupNameOptions("REC_CHK_CARD_ACTION", "", false) %>
				</select>
		    </td>
		</tr>
		<tr>
			<td><span id="p_action_title_inval"><%= Bean.transactionXML.getfieldTransl("action", true) %></span>
            </td>
			<td>
	            <select name="p_action_inval" id="p_action_inval" class="inputfield">
					<%= Bean.getMeaningFromLookupNameOptions("REC_INVAL_CARD_ACTION", "", false) %>
				</select>
		    </td>
		</tr>
		<tr>
			<td colspan=8 class="top_line"><b><%= Bean.transactionXML.getfieldTransl("h_param_operation", false) %></b></td>
		</tr>
		<tr>
			<td valign="top"><span id="span_opr_sum"><%= Bean.transactionXML.getfieldTransl("opr_sum", false) %></span></td><td><input type="text" name="opr_sum" id="opr_sum" size="20" value="" class="inputfield" title="OPR_SUM"></td>
			<td valign="top"><span id="span_sum_bon"><%= Bean.transactionXML.getfieldTransl("sum_bon", false) %></span></td><td><input type="text" name="sum_bon" id="sum_bon" size="20" value="" class="inputfield" title="SUM_BON"></td>
		</tr>
		<tr>
			<td valign="top"><span id="span_sum_pay_cash"><%= Bean.transactionXML.getfieldTransl("sum_pay_cash", false) %></span></td><td><input type="text" name="sum_pay_cash" id="sum_pay_cash" size="20" value="" class="inputfield" title="SUM_PAY_CASH"></td>
			<td valign="top"><span id="span_sum_bon_cash"><%= Bean.transactionXML.getfieldTransl("sum_bon_cash", false) %></span></td><td><input type="text" name="sum_bon_cash" id="sum_bon_cash" size="20" value="" readonly="readonly" class="inputfield-ro"> </td>
		</tr>
		<tr>
			<td valign="top"><span id="span_sum_pay_card"><%= Bean.transactionXML.getfieldTransl("sum_pay_card", false) %></span></td><td><input type="text" name="sum_pay_card" id="sum_pay_card" size="20" value="" class="inputfield" title="SUM_PAY_CARD"> </td>
			<td valign="top"><span id="span_sum_bon_card"><%= Bean.transactionXML.getfieldTransl("sum_bon_card", false) %></span></td><td><input type="text" name="sum_bon_card" id="sum_bon_card" size="20" value="" readonly="readonly" class="inputfield-ro"> </td>
		</tr>
		<tr>
			<td valign="top"><span id="span_sum_pay_bon"><%= Bean.transactionXML.getfieldTransl("sum_pay_bon", false) %></span></td><td><input type="text" name="sum_pay_bon" id="sum_pay_bon" size="20" value="" class="inputfield" title="SUM_PAY_BON"> </td>
			<td valign="top"><span id="span_sum_bon_bon"><%= Bean.transactionXML.getfieldTransl("sum_bon_bon", false) %></span></td><td><input type="text" name="sum_bon_bon" id="sum_bon_bon" size="20" value="" readonly="readonly" class="inputfield-ro"> </td>
		</tr>
		<tr>
			<td valign="top"><span id="span_club_sum"><%= Bean.transactionXML.getfieldTransl("club_sum", false) %></span></td> <td><input type="text" name="club_sum" id="club_sum" size="20" value="" class="inputfield" title="CLUB_SUM"></td>
			<td valign="top"><span id="span_sum_disc"><%= Bean.transactionXML.getfieldTransl("sum_disc", false) %></span></td><td><input type="text" name="sum_disc" id="sum_disc" size="20" value="" class="inputfield" title="SUM_DISC"></td>
		</tr>
		<tr>
			<td valign="top" colspan="2">&nbsp;</td>
			<td valign="top"><span id="span_sum_bon_disc"><%= Bean.transactionXML.getfieldTransl("sum_bon_disc", false) %></span></td><td><input type="text" name="sum_bon_disc" id="sum_bon_disc" size="20" value="" readonly="readonly" class="inputfield-ro"> </td>
		</tr>
		<tr>
			<td colspan=8 class="top_line"><b><%= Bean.transactionXML.getfieldTransl("h_param_after", false) %></b></td>
		</tr>
		<tr>
			<td valign="top"><span id="span_club_st"><%= Bean.transactionXML.getfieldTransl("club_st", false) %></span></td> <td><input type="text" name="club_st" id="club_st" size="20" value="" class="inputfield" title="CLUB_ST"></td>
		</tr>
		<tr>
			<td valign="top"><span id="span_club_disc"><%= Bean.transactionXML.getfieldTransl("club_disc", false) %></span></td><td><input type="text" name="club_disc" id="club_disc" size="20" value="" class="inputfield" title="CLUB_DISC"> </td>
			<td valign="top"><span id="span_bal_acc"><%= Bean.transactionXML.getfieldTransl("bal_acc", false) %></span></td> <td><input type="text" name="bal_acc" id="bal_acc" size="20" value="" class="inputfield" title="BAL_ACC"> </td>
		</tr>
		<tr>
			<td valign="top"><span id="span_club_bon"><%= Bean.transactionXML.getfieldTransl("club_bon", false) %></span></td><td><input type="text" name="club_bon" id="club_bon" size="20" value="" class="inputfield" title="CLUB_BON"></td>
			<td valign="top"><span id="span_bal_cur"><%= Bean.transactionXML.getfieldTransl("bal_cur", false) %></span></td><td><input type="text" name="bal_cur" id="bal_cur" size="20" value="" class="inputfield" title="BAL_CUR"></td>
		</tr>
		<tr>
			<td valign="top"><span id="span_date_acc"><%= Bean.transactionXML.getfieldTransl("date_acc", false) %></span></td><td><input type="text" name="date_acc" id="date_acc" size="20" value="" class="inputfield" title="DATE_ACC"></td>
			<td valign="top"><span id="span_bal_bon_per"><%= Bean.transactionXML.getfieldTransl("bal_bon_per", false) %></span></td><td><input type="text" name="bal_bon_per" id="bal_bon_per" size="20" value="" class="inputfield" title="BAL_BON_PER"></td>
		</tr>
		<tr>
			<td valign="top"><span id="span_date_mov"><%= Bean.transactionXML.getfieldTransl("date_mov", false) %></span></td><td><input type="text" name="date_mov" id="date_mov" size="20" value="" class="inputfield" title="DATE_MOV"></td>
			<td valign="top"><span id="span_bal_disc_per"><%= Bean.transactionXML.getfieldTransl("bal_disc_per", false) %></span></td><td><input type="text" name="bal_disc_per" id="bal_disc_per" size="20" value="" class="inputfield" title="BAL_DISC_PER"> </td>
		</tr>
		
		<tr>
			<td colspan=8 class="top_line"><b><%= Bean.transactionXML.getfieldTransl("h_param_im", false) %></b></td>
		</tr>
		<tr>
			<td valign="top"><span id="span_imid"><%= Bean.transactionXML.getfieldTransl("imid", false) %></span></td><td><input type="text" name="imid" id="imid" size="20" value="" class="inputfield" title="IMID"></td>
			<td valign="top"><span id="span_specid"><%= Bean.transactionXML.getfieldTransl("specid", false) %></span></td><td><input type="text" name="specid" id="specid" size="20" value="" class="inputfield" title="SPECID"></td>
		</tr>
		<tr>
			<td valign="top"><span id="span_cardid"><%= Bean.transactionXML.getfieldTransl("cardid", false) %></span></td><td><input type="text" name="cardid" id="cardid" size="20" value="" class="inputfield" title="CARDID"></td>
			<td valign="top" colspan="2">&nbsp;</td>
		</tr>
		
		<tr>
			<td colspan=8 class="top_line"><b><%= Bean.transactionXML.getfieldTransl("h_param_check", false) %></b></td>
		</tr>
		<tr>
			<td><span id="span_fl_ext_loyl"><%= Bean.transactionXML.getfieldTransl("fl_ext_loyl", false) %></span></td> <td><select name="fl_ext_loyl" id="fl_ext_loyl" class="inputfield" title="FL_EXT_LOYL"><option value=""></option><%= Bean.getMeaningFromLookupNumberOptions("YES_NO", "0", false) %></select></td>
			<td><span id="span_c_nr"><%= Bean.transactionXML.getfieldTransl("c_nr", false) %></span></td><td><input type="text" name="c_nr" id="c_nr" size="20" value="0" class="inputfield" title="C_NR"></td>
		</tr>
		<tr>
			<td><span id="span_cash_card_nr"><%= Bean.transactionXML.getfieldTransl("cash_card_nr", false) %></span></td><td><input type="text" name="cash_card_nr" id="cash_card_nr" size="20" value="0" class="inputfield" title="CASH_CARD_NR"></td>
			<td><span id="span_c_check_nr"><%= Bean.transactionXML.getfieldTransl("c_check_nr", false) %></span></td><td><input type="text" name="c_check_nr" id="c_check_nr" size="20" value="0" class="inputfield" title="C_CHECK_NR"></td>
		</tr>
		<tr>
			<td colspan="4" class="top_line"><b><%= Bean.transactionXML.getfieldTransl("h_rec_payment_ext_param", false) %></b></td>
		</tr>
		<tr>
			<td valign="top"><span id="span_c_nomenkl"><%= Bean.transactionXML.getfieldTransl("c_nomenkl", false) %></span></td><td  colspan="3"><input type="text" name="c_nomenkl" id="c_nomenkl" size="74" value="" class="inputfield" title="C_NOMENKL"></td>
		</tr>

		<tr>
			<td colspan=8 class="top_line"><b><%= Bean.transactionXML.getfieldTransl("h_user_changing", false) %></b></td>
		</tr>
		<tr>
			<td valign="top"><%= Bean.transactionXML.getfieldTransl("bases_for_changes", true) %></td><td colspan=7><textarea name="bases_for_changes" id="bases_for_changes" cols="100" rows="3" class="inputfield"></textarea></td>
		</tr>
		<tr>
			<td colspan="8" align="center">
				<%=Bean.getSubmitButtonAjax("../crm/cards/trans_correctionupdate.jsp") %>
				<%=Bean.getResetButton() %>
				<% if (!(id_trans==null || "".equalsIgnoreCase(id_trans))) { %>
					<%=Bean.getGoBackButton("../crm/cards/trans_correctionupdate.jsp?id_trans="+id_trans+"&process=no&action=add2") %>
				<% } else { %>
					<%=Bean.getGoBackButton("../crm/cards/trans_correctionupdate.jsp?process=no&action=add") %>
				<% } %>
			</td>
		</tr>

		<% } else if ("SOURCE".equalsIgnoreCase(input_transaction_type)) { %>
		<tr>
				<% String id_club = Bean.getCurrentClubID(); %>
				<td><%= Bean.clubXML.getfieldTransl("club", true) %>
					<%= Bean.getGoToClubLink(id_club) %>
			  	</td>
			  	<td>
					<%=Bean.getWindowFindClub("club", id_club, "25") %>
			  	</td>

		</tr>
		<tr>
			<td><%= Bean.transactionXML.getfieldTransl("type_trans_txt", true) %></td> <td><select name="type_trans" class="inputfield"><%= Bean.getTransTypeOptions("1", true) %></select></td>
		</tr>
		<tr>
			<td><%= Bean.transactionXML.getfieldTransl("ver_trans", true) %></td> <td><input type="text" name="ver_trans" size="20" value="" class="inputfield"></td>
		</tr>
		<tr>
			<td><%= Bean.transactionXML.getfieldTransl("is_crypted", true) %></td> <td><select name="is_crypted" class="inputfield"><%= Bean.getMeaningFromLookupNumberOptions("YES_NO", "0", false) %></select></td>
		</tr>
		<tr>
			<td valign="top"><%= Bean.transactionXML.getfieldTransl("src_trans", true) %></td> 
				<td colspan="3"><textarea name="src_trans" cols="150" rows="3" class="inputfield"></textarea></td>
		</tr>
		<tr>
			<td colspan="6" align="center">
				<%=Bean.getSubmitButtonAjax("../crm/cards/trans_correctionupdate.jsp") %>
				<%=Bean.getResetButton() %>
				<% if (!(id_trans==null || "".equalsIgnoreCase(id_trans))) { %>
					<%=Bean.getGoBackButton("../crm/cards/trans_correctionupdate.jsp?id_trans="+id_trans+"&process=no&action=add2") %>
				<% } else { %>
					<%=Bean.getGoBackButton("../crm/cards/trans_correctionupdate.jsp?process=no&action=add") %>
				<% } %>
			</td>
		</tr>
		<% } %>
	</table>
	</form>
	
        <%
	    	} else {
	    	    %> <%= Bean.getUnknownActionText(action) %><%
	    	}
		}

else if (process.equalsIgnoreCase("yes")) {
	
    String  
		id_term					= Bean.getDecodeParam(parameters.get("id_term")),
		type_trans				= Bean.getDecodeParam(parameters.get("type_trans")),
		ver_trans				= Bean.getDecodeParam(parameters.get("ver_trans")),
		state_trans				= Bean.getDecodeParam(parameters.get("state_trans")),
		id_sam					= Bean.getDecodeParam(parameters.get("id_sam")),
		nt_sam					= Bean.getDecodeParam(parameters.get("nt_sam")),
		vk_enc					= Bean.getDecodeParam(parameters.get("vk_enc")),
		card_nr					= Bean.getDecodeParam(parameters.get("card_nr")),
		cd_issuer				= Bean.getDecodeParam(parameters.get("cd_issuer")),
		cd_paym_sys				= Bean.getDecodeParam(parameters.get("cd_paym_sys")),
		curr_pda				= Bean.getDecodeParam(parameters.get("curr_pda")),
		nt_icc					= Bean.getDecodeParam(parameters.get("nt_icc")),
		nt_ext					= Bean.getDecodeParam(parameters.get("nt_ext")),
		sys_date				= Bean.getDecodeParam(parameters.get("sys_date")),
		sys_time				= Bean.getDecodeParam(parameters.get("sys_time")),
		mac_icc 				= Bean.getDecodeParam(parameters.get("mac_icc")),
		mac_pda 				= Bean.getDecodeParam(parameters.get("mac_pda")),
		p_action				= "",
		p_action_chk			= Bean.getDecodeParam(parameters.get("p_action_chk")),
		p_action_inval			= Bean.getDecodeParam(parameters.get("p_action_inval")),
		opr_sum					= Bean.getDecodeParam(parameters.get("opr_sum")),
		sum_pay_cash			= Bean.getDecodeParam(parameters.get("sum_pay_cash")),
		sum_pay_card			= Bean.getDecodeParam(parameters.get("sum_pay_card")),
		sum_pay_bon				= Bean.getDecodeParam(parameters.get("sum_pay_bon")),
		fl_ext_loyl				= Bean.getDecodeParam(parameters.get("fl_ext_loyl")),
		club_sum				= Bean.getDecodeParam(parameters.get("club_sum")),
		sum_bon					= Bean.getDecodeParam(parameters.get("sum_bon")),
		sum_disc				= Bean.getDecodeParam(parameters.get("sum_disc")),
		cash_card_nr			= Bean.getDecodeParam(parameters.get("cash_card_nr")),
		c_nr					= Bean.getDecodeParam(parameters.get("c_nr")),
		c_check_nr				= Bean.getDecodeParam(parameters.get("c_check_nr")),
		club_st					= Bean.getDecodeParam(parameters.get("club_st")),
		club_disc				= Bean.getDecodeParam(parameters.get("club_disc")),
		club_bon				= Bean.getDecodeParam(parameters.get("club_bon")),
		date_acc				= Bean.getDecodeParam(parameters.get("date_acc")),
		date_mov				= Bean.getDecodeParam(parameters.get("date_mov")),
		bal_acc					= Bean.getDecodeParam(parameters.get("bal_acc")),
		bal_cur					= Bean.getDecodeParam(parameters.get("bal_cur")),
		bal_bon_per				= Bean.getDecodeParam(parameters.get("bal_bon_per")),
		bal_disc_per			= Bean.getDecodeParam(parameters.get("bal_disc_per")),
		err_tx					= Bean.getDecodeParam(parameters.get("err_tx")),
		bases_for_changes		= Bean.getDecodeParam(parameters.get("bases_for_changes")),
		is_crypted				= Bean.getDecodeParam(parameters.get("is_crypted")),
		src_trans				= Bean.getDecodeParam(parameters.get("src_trans")),
		imid					= Bean.getDecodeParam(parameters.get("imid")),
		specid					= Bean.getDecodeParam(parameters.get("specid")),
		clubcard				= Bean.getDecodeParam(parameters.get("clubcard")),
		cardid					= Bean.getDecodeParam(parameters.get("cardid")),
		c_nomenkl				= Bean.getDecodeParam(parameters.get("c_nomenkl")),
		id_club					= Bean.getDecodeParam(parameters.get("id_club")),
    	club_st_prv				= Bean.getDecodeParam(parameters.get("club_st_prv")),
    	club_disc_prv			= Bean.getDecodeParam(parameters.get("club_disc_prv")),
    	club_bon_prv			= Bean.getDecodeParam(parameters.get("club_bon_prv")),
    	date_acc_prv			= Bean.getDecodeParam(parameters.get("date_acc_prv")),
    	date_mov_prv			= Bean.getDecodeParam(parameters.get("date_mov_prv")),
    	date_calc_prv			= Bean.getDecodeParam(parameters.get("date_calc_prv")),
    	bal_acc_prv				= Bean.getDecodeParam(parameters.get("bal_acc_prv")),
    	bal_cur_prv				= Bean.getDecodeParam(parameters.get("bal_cur_prv")),
    	bal_bon_per_prv			= Bean.getDecodeParam(parameters.get("bal_bon_per_prv")),
    	bal_disc_per_prv		= Bean.getDecodeParam(parameters.get("bal_disc_per_prv")),
    	id_trans_reversed		= Bean.getDecodeParam(parameters.get("id_trans_reversed")),
    	id_trans_double			= Bean.getDecodeParam(parameters.get("id_trans_double")),
    	fl_has_format_error		= Bean.getDecodeParam(parameters.get("fl_has_format_error")),
    	entered_manually		= Bean.getDecodeParam(parameters.get("entered_manually"));

	
	
	if ("3".equalsIgnoreCase(type_trans)) {
		p_action = p_action_chk;
	} else if ("4".equalsIgnoreCase(type_trans)) {
		p_action = p_action_inval;
	} else {
		p_action = "";
	}
    String callSQL = "";

	if (action.equalsIgnoreCase("add")) { 

		String input_transaction_type 	= Bean.getDecodeParam(parameters.get("input_transaction_type")); 
   	
	    if ("PARSED".equalsIgnoreCase(input_transaction_type)) {
	    	callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK$TRANS_UI_ERROR.add_parsed_trans("+
	    		"?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";

		    String[] pParam = new String [47];
		    			
		    pParam[0] = id_club;
		    pParam[1] = id_term;
		    pParam[2] = type_trans;
		    pParam[3] = ver_trans;
		    pParam[4] = state_trans;
		    pParam[5] = id_sam;
		    pParam[6] = nt_sam;
		    pParam[7] = vk_enc;
		    pParam[8] = card_nr;
		    pParam[9] = cd_issuer;
		    pParam[10] = cd_paym_sys;
		    pParam[11] = curr_pda;
		    pParam[12] = nt_icc;
		    pParam[13] = nt_ext;
		    pParam[14] = sys_date;
		    pParam[15] = sys_time;
		    pParam[16] = mac_icc;
		    pParam[17] = mac_pda;
		    pParam[18] = p_action;
		    pParam[19] = opr_sum;
		    pParam[20] = sum_pay_cash;
		    pParam[21] = sum_pay_card;
		    pParam[22] = sum_pay_bon;
		    pParam[23] = fl_ext_loyl;
		    pParam[24] = club_sum;
		    pParam[25] = sum_bon;
		    pParam[26] = sum_disc;
		    pParam[27] = cash_card_nr;
		    pParam[28] = c_nr;
		    pParam[29] = c_check_nr;
		    pParam[30] = club_st;
		    pParam[31] = club_disc;
		    pParam[32] = club_bon;
		    pParam[33] = date_acc;
		    pParam[34] = date_mov;
		    pParam[35] = bal_acc;
		    pParam[36] = bal_cur;
		    pParam[37] = bal_bon_per;
		    pParam[38] = bal_disc_per;
		    pParam[39] = imid;
		    pParam[40] = specid;
		    pParam[41] = clubcard;
		    pParam[42] = cardid;
		    pParam[43] = c_nomenkl;
		    pParam[44] = err_tx;
		    pParam[45] = bases_for_changes;
		    pParam[46] = Bean.getDateFormat();
		 	%>
			<%= Bean.getCallResultParam("INSERT", callSQL, pParam, "../crm/cards/trans_correctionspecs.jsp?id=" , "../crm/cards/trans_correction.jsp") %>
			<% 	
	    } else if ("SOURCE".equalsIgnoreCase(input_transaction_type)) {
	    	callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK$TRANS_UI_ERROR.add_source_trans("+
	    		"?,?,?,?,?,?,?)}"; 

	    	String[] pParam = new String [5];
	    			
	    	pParam[0] = id_club;
	    	pParam[1] = type_trans;
	    	pParam[2] = ver_trans;
	    	pParam[3] = is_crypted;
	    	pParam[4] = src_trans;

		 	%>
			<%= Bean.getCallResultParam("INSERT", callSQL, pParam, "../crm/cards/trans_correctionspecs.jsp?id=" , "../crm/cards/trans_correction.jsp") %>
			<% 	
	    }

   	} else if (action.equalsIgnoreCase("remove")) { 
     	
       	callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK$TRANS_UI_ERROR.delete_trans(?,?,?)}";

	    String[] pParam = new String [5];
	    			
	    pParam[0] = err_type;
	    pParam[1] = id_trans;
  	     
	 	%>
		<%= Bean.getCallResultParam("DELETE", callSQL, pParam, "../crm/cards/trans_correction.jsp" , "") %>
		<% 	
  	    
	} else if (action.equalsIgnoreCase("edit")) { 
   	
        	callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK$TRANS_UI_ERROR.update_trans("+
	     		"?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?," + 
	     		"?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";

			String[] pParam = new String [64];
		    			
			pParam[0] = err_type;
			pParam[1] = id_trans;
			pParam[2] = id_term;
		    pParam[3] = type_trans;
		    pParam[4] = ver_trans;
		    pParam[5] = state_trans;
		    pParam[6] = id_sam;
		    pParam[7] = nt_sam;
		    pParam[8] = vk_enc;
		    pParam[9] = card_nr;
		    pParam[10] = cd_issuer;
		    pParam[11] = cd_paym_sys;
		    pParam[12] = curr_pda;		    
		    pParam[13] = nt_icc;
		    pParam[14] = nt_ext;
		    pParam[15] = sys_date;
		    pParam[16] = sys_time;
		    pParam[17] = mac_icc;
		    pParam[18] = mac_pda;
		    pParam[19] = p_action;
	     	pParam[20] = club_st_prv;
	     	pParam[21] = club_disc_prv;
	     	pParam[22] = club_bon_prv;
	     	pParam[23] = date_acc_prv;
	     	pParam[24] = date_mov_prv;
	     	pParam[25] = date_calc_prv;
	     	pParam[26] = bal_acc_prv;
	     	pParam[27] = bal_cur_prv;
	     	pParam[28] = bal_bon_per_prv;
	     	pParam[29] = bal_disc_per_prv;
		    pParam[30] = opr_sum;
		    pParam[31] = sum_pay_cash;
		    pParam[32] = sum_pay_card;
		    pParam[33] = sum_pay_bon;
		    pParam[34] = fl_ext_loyl;
		    pParam[35] = club_sum;
		    pParam[36] = sum_bon;
		    pParam[37] = sum_disc;
		    pParam[38] = cash_card_nr;
		    pParam[39] = c_nr;
		    pParam[40] = c_check_nr;
		    pParam[41] = club_st;
		    pParam[42] = club_disc;
		    pParam[43] = club_bon;
		    pParam[44] = date_acc;
		    pParam[45] = date_mov;
		    pParam[46] = bal_acc;
		    pParam[47] = bal_cur;
		    pParam[48] = bal_bon_per;
		    pParam[49] = bal_disc_per;
		    pParam[50] = imid;
		    pParam[51] = specid;
		    pParam[52] = clubcard;
		    pParam[53] = cardid;
		    pParam[54] = c_nomenkl;
		    pParam[55] = err_tx;
		    pParam[56] = id_trans_reversed;
		    pParam[57] = id_trans_double;
		    pParam[58] = bases_for_changes;
		    pParam[59] = is_crypted;
		    pParam[60] = src_trans;
		    pParam[61] = fl_has_format_error;
		    pParam[62] = entered_manually;
		    pParam[63] = Bean.getDateFormat();
	     
	 	%>
		<%= Bean.getCallResultParam("UPDATE", callSQL, pParam, "../crm/cards/trans_correctionspecs.jsp?id=" + id_trans, "") %>
		<% 	

   	} else if (action.equalsIgnoreCase("check")) { 
	     	
	   	callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK$TRANS_UI_ERROR.check_transactions(?,?,?)}";

		String[] pParam = new String [2];
				
		pParam[0] = err_type;
		pParam[1] = id_trans;
	  	     
	 	%>
		<%= Bean.getCallResultParam("UPDATE", callSQL, pParam, "../crm/cards/trans_correctionspecs.jsp?id=" + id_trans, "") %>
		<% 	
 
   	} else { %> 
   		<%= Bean.getUnknownActionText(action) %><% 
   	}
} else { %> 
	<%= Bean.getUnknownProcessText(process) %> <%
}
%>
</body>
</html>