<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcWebPosBean"  />

<%@page import="java.util.HashMap"%>
<%@page import="webpos.wpClubCardObject"%>
<%@page import="java.util.ArrayList"%>
<%@page import="webpos.wpChequeObject"%>
<%@page import="webpos.wpNatPrsRoleObject"%>
<%@page import="webpos.wpNatPrsObject"%><html>
<head>
	<%= Bean.getMetaContent() %>

</head>

<body>
<%
HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());
System.out.println("parameters="+parameters);

String pageFormName = "WEBPOS_CHECK_CARD";

Bean.setJspPageForTabName(pageFormName);

request.setCharacterEncoding("UTF-8");
String action	= Bean.getDecodeParam(parameters.get("action")); 

action 	= Bean.isEmpty(action)?"":action;

String
	id_telgr		= Bean.getDecodeParam(parameters.get("id_telgr")),
	back_type		= Bean.getDecodeParam(parameters.get("back_type")),
	back_div		= Bean.getDecodeParam(parameters.get("back_div"));

String pBackForm = "";
String pSubmitForm = "";
back_div 	= Bean.isEmpty(back_div)?"div_main":back_div;

if ("pay".equalsIgnoreCase(back_type)) {
	pBackForm = "action/pay.jsp";
	pSubmitForm = "action/payupdate.jsp";
} else if ("membership_fee".equalsIgnoreCase(back_type)) {
	pBackForm = "action/replenish.jsp";
	pSubmitForm = "action/replenishupdate.jsp";
	back_div = "div_main";
} else if ("replenish".equalsIgnoreCase(back_type)) {
	pBackForm = "action/replenishupdate.jsp";
	pSubmitForm = "action/replenishupdate.jsp";
	back_div = "div_action_big";
} else if ("transfer".equalsIgnoreCase(back_type)) {
	pBackForm = "action/transfer.jsp";
	pSubmitForm = "action/transferupdate.jsp";
} else if ("storno".equalsIgnoreCase(back_type)) {
	pBackForm = "action/storno.jsp";
	pSubmitForm = "action/stornoupdate.jsp";
} else if ("oper".equalsIgnoreCase(back_type)) {
	pBackForm = "report/operation.jsp";
	pSubmitForm = "report/operation.jsp";
} else if ("invoice".equalsIgnoreCase(back_type)) {
	pBackForm = "report/operationupdate.jsp";
	pSubmitForm = "report/invoicepay.jsp";
} else {
	pBackForm = "action/pay.jsp";
	pSubmitForm = "action/payupdate.jsp";
}

Bean.loginTerm.getFeature();

Bean.readWebPosMenuHTML();

%>

<% if (!Bean.hasTerminalPermission(Bean.loginTerm)) { %>
	<%=Bean.getErrorPermissionMessage(pageFormName, Bean.loginTerm) %>
	<% action = "error_term"; %>
<% } %>
<%
if (action.equalsIgnoreCase("error_term")) {
} else { 
	if (action.equalsIgnoreCase("get_sms_code")) { 
			
			ArrayList<String> pParam = new ArrayList<String>();
			
			pParam.add(id_telgr);
			
			String[] results = new String[2];
			
			results 						= Bean.executeFunction("PACK$WEBPOS_UI.create_confirm_sms", pParam, results.length);
			String resultInt 				= results[0];
			String resultMessage 			= results[1];
	    	
	    	if (Bean.C_SUCCESS_RESULT.equalsIgnoreCase(resultInt)) {
	    		wpChequeObject oper = new wpChequeObject(id_telgr, "TXT", Bean.loginTerm);
	    		wpNatPrsObject nat_prs = new wpNatPrsObject(oper.getValue("ID_NAT_PRS"));
	    %>
				<% if (!Bean.isEmpty(resultMessage)) { %>
				
				<% if (!Bean.isEmpty(nat_prs.getValue("FULL_PHOTO_FILE_NAME"))) { %>
				<div class="photo_rect_small" id="div_photo" style="float:right;margin:7px 0 7px 7px;">
					<img src="../NatPrsPhoto?id_nat_prs=<%=oper.getValue("ID_NAT_PRS") %>&noCache=<%=Bean.getNoCasheValue() %>" class="photo_image_small">
				</div>
				<% } %>
				<span id="succes_description"><%=resultMessage %></span><br>
				<% } %>
				<% 
					int signatureSendCount = 0; 
					if (!Bean.isEmpty(oper.getValue("SMS_SIGNATURE_SEND_COUNT"))) {
						signatureSendCount = Integer.parseInt(oper.getValue("SMS_SIGNATURE_SEND_COUNT"));
					}
					if (signatureSendCount < Bean.C_SIGNATURE_MAX_SEND_COUNT) {
				%>
				<span>Не получили СМС? <span class="go_to" onclick="ajaxpage('service/get_sms_code.jsp?id_telgr=<%= id_telgr%>&action=get_sms_code&back_type=<%= back_type%>', 'div_sms_confirmation')" title="<%=Bean.buttonXML.getfieldTransl("button_questionnaire", false) %>">Отправить еще раз</span></span>
				<br>
				<% } else { %>
				<br>
				<% } %>
				<br>
				<span class="need_sms_confirmation"><%=Bean.webposXML.getfieldTransl("sms_password", false) %></span>&nbsp;&nbsp;&nbsp;<input class="inputfield" id="confirm_code" name="confirm_code" placeholder="<%=Bean.C_CONFIRM_OPER_SMS_PLACEHOLDER %>" autofocus="" required="" type="password" size="10" value="" maxlength="<%=Bean.C_CONFIRM_OPER_SMS_PLACEHOLDER.length() %>"><br><br>
				<div style="width:100%; text-align: center;">
				    <%=Bean.getSubmitButtonAjax(pSubmitForm, "confirm", "updateForm", "div_action_big", "validateData") %>
				    <%=Bean.getSubmitButtonAjax(pBackForm, "button_back", "updateFormBack", back_div) %>
				</div>
	    <%	} else { %>
				<% if (!Bean.isEmpty(resultMessage)) { %>
				<span id="error_title"><%=Bean.webposXML.getfieldTransl("operation_error", false) %></span><br>
				<span id="error_description"><%=resultMessage %></span>
				<% } %>
				<div style="width:100%; text-align: center;">
				    <%=Bean.getSubmitButtonAjax("service/get_sms_code.jsp", "button_get_sms", "updateFormGetSMS", "div_sms_confirmation", "") %>
				    <%=Bean.getSubmitButtonAjax(pBackForm, "button_back", "updateFormBack", back_div) %>
				</div>
			<% }
    } else { %> 
		<%= Bean.getUnknownActionText(action) %>
		<br>	
		<div style="width:100%; text-align: center;">
		    <%=Bean.getSubmitButtonAjax("action/replenishupdate.jsp", "confirm", "updateForm", "div_action_big", "validateData") %>
		    <%=Bean.getSubmitButtonAjax(pBackForm, "button_back", "updateFormBack", back_div) %>
		</div>
<%	}
}
%>


</body>
</html>
