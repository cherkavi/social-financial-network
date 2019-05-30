<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcWebPosBean"  />

<%@page import="java.util.HashMap"%>
<%@page import="java.sql.Connection"%>
<%@page import="BonCard.Reports.ReportVariables"%>
<%@page import="webpos.wpFNInvoiceObject"%>
<%@page import="java.util.ArrayList"%><html>
<%

request.setCharacterEncoding("UTF-8");

String pageFormName = "WEBPOS_SERVICE_OPERATION";
String tagReport = "_REPORT";
String tagReportFind = "_REPORT_FIND";

String tagTransaction = "_TRANSACTION";
String tagTransactionFind = "_TRANSACTION_FIND";

String tagInvoice = "_INVOICE";
String tagInvoiceFind = "_INVOICE_FIND";

String tagContent = "_CONTENT";
String tagContentFind = "_CONTENT_FIND";

String tagRRNFind = "_RRN_FIND";
String tagCdCard1Find = "_CD_CARD1_FIND";
String tagOperationType = "_OPERATION_TYPE";
String tagOperationState = "_OPERATION_STATE";
String tagPayType = "_PAY_TYPE";

Bean.readWebPosMenuHTML();

Bean.setJspPageForTabName(pageFormName);

HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());

String tab = Bean.getDecodeParam(parameters.get("tab"));
if (tab == null || "".equalsIgnoreCase(tab)) {
	tab = Bean.tabsHmGetValue(pageFormName);
}

boolean tab1HasPermission = Bean.hasReadMenuPermission("WEBPOS_SERVICE_OPERATION_OPERATIONS");
boolean tab2HasPermission = Bean.hasReadMenuPermission("WEBPOS_SERVICE_OPERATION_REPORTS");
boolean tab3HasPermission = Bean.hasReadMenuPermission("WEBPOS_SERVICE_OPERATION_INVOICES");
int tabCount = (tab1HasPermission?1:0)+(tab2HasPermission?1:0)+(tab3HasPermission?1:0);

tab = (Integer.parseInt(tab)>3)?"1":tab;

if ("1".equalsIgnoreCase(tab) && !tab1HasPermission) {
	tab = !tab2HasPermission?(!tab3HasPermission?"4":"3"):"2";
} else if ("2".equalsIgnoreCase(tab) && !tab2HasPermission) {
	tab = !tab1HasPermission?(!tab3HasPermission?"4":"3"):"1";
} else if ("3".equalsIgnoreCase(tab) && !tab3HasPermission) {
	tab = !tab1HasPermission?(!tab2HasPermission?"4":"2"):"1";
}

Bean.tabsHmSetValue(pageFormName, tab);

Bean.loginTerm.getTermFeature();

String l_report_page = Bean.getDecodeParam(parameters.get("report_page"));
Bean.pageCheck(pageFormName + tagReport, l_report_page);
String l_report_page_beg = Bean.getFirstRowNumber(pageFormName + tagReport);
String l_report_page_end = Bean.getLastRowNumber(pageFormName + tagReport);

String report_find 	= Bean.getDecodeParam(parameters.get("report_find"));
report_find 	= Bean.checkFindString(pageFormName + tagReportFind, report_find, l_report_page);


String l_transaction_page = Bean.getDecodeParam(parameters.get("transaction_page"));
Bean.pageCheck(pageFormName + tagTransaction, l_transaction_page);
String l_transaction_page_beg = Bean.getFirstRowNumber(pageFormName + tagTransaction);
String l_transaction_page_end = Bean.getLastRowNumber(pageFormName + tagTransaction);

String transaction_find 	= Bean.getDecodeParam(parameters.get("transaction_find"));
transaction_find 		= Bean.checkFindString(pageFormName + tagTransactionFind, transaction_find, l_transaction_page);

String operation_type 	= Bean.getDecodeParam(parameters.get("operation_type"));
operation_type 			= Bean.checkFindString(pageFormName + tagOperationType, operation_type, l_transaction_page);

String operation_state 	= Bean.getDecodeParam(parameters.get("operation_state"));
operation_state			= Bean.checkFindString(pageFormName + tagOperationState, operation_state, l_transaction_page);

String pay_type 		= Bean.getDecodeParam(parameters.get("pay_type"));
pay_type 				= Bean.checkFindString(pageFormName + tagPayType, pay_type, l_transaction_page);

String cd_card1_find 	= Bean.getDecodeParam(parameters.get("cd_card1_find"));
cd_card1_find 			= Bean.checkFindString(pageFormName + tagCdCard1Find, cd_card1_find, l_transaction_page);

String rrn_find 		= Bean.getDecodeParam(parameters.get("rrn_find"));
rrn_find 				= Bean.checkFindString(pageFormName + tagRRNFind, rrn_find, l_transaction_page);

String l_invoice_page = Bean.getDecodeParam(parameters.get("invoice_page"));
Bean.pageCheck(pageFormName + tagInvoice, l_invoice_page);
String l_invoice_page_beg = Bean.getFirstRowNumber(pageFormName + tagInvoice);
String l_invoice_page_end = Bean.getLastRowNumber(pageFormName + tagInvoice);

String invoice_find 	= Bean.getDecodeParam(parameters.get("invoice_find"));
invoice_find 			= Bean.checkFindString(pageFormName + tagInvoiceFind, invoice_find, l_invoice_page);

//Обрабатываем номера страниц
String l_content_page = Bean.getDecodeParam(parameters.get("content_page"));
Bean.pageCheck(pageFormName + tagContent, l_content_page);
String l_content_page_beg = Bean.getFirstRowNumber(pageFormName + tagContent);
String l_content_page_end = Bean.getLastRowNumber(pageFormName + tagContent);

String content_find	 	= Bean.getDecodeParam(parameters.get("content_find"));
content_find 				= Bean.checkFindString(pageFormName + tagContentFind, content_find, l_content_page);

String id_invoice		= Bean.getDecodeParam(parameters.get("id_invoice"));

String report_number=Bean.getDecodeParam(parameters.get("id_report"));

String action = Bean.getDecodeParam(parameters.get("action"));
if (action == null || "".equalsIgnoreCase(action)) {
	if ("1".equalsIgnoreCase(tab)) {
		if (Bean.isOperationPeriodSet) {
			action = "show";
		} else {
			action = "param";
		}
	} else if ("2".equalsIgnoreCase(tab)) {
		if (Bean.isEmpty(report_number))  {
			action = "param";
		} else {
			action = "show";
		}
	} else if ("3".equalsIgnoreCase(tab)) {
		if (Bean.isInvoicePeriodSet) {
			action = "invshow";
		} else {
			action = Bean.isEmpty(action)?"invparam":"invdetail";
		}
	}
}

String
	number_invoice		= Bean.getDecodeParamPrepare(parameters.get("number_invoice")),
	create_automatic	= Bean.getDecodeParamPrepare(parameters.get("create_automatic")),
	date_invoice		= Bean.getDecodeParamPrepare(parameters.get("date_invoice")),
	cd_fn_priority		= Bean.getDecodeParamPrepare(parameters.get("cd_fn_priority")),
	begin_period_date	= Bean.getDecodeParamPrepare(parameters.get("begin_period_date")),
	end_period_date		= Bean.getDecodeParamPrepare(parameters.get("end_period_date")),
	paid_sum			= Bean.getDecodeParamPrepare(parameters.get("paid_sum")),
	LUD					= Bean.getDecodeParamPrepare(parameters.get("LUD"));

String resultInt 				= Bean.C_SUCCESS_RESULT;
String resultMessage 			= "";

%>
<body>
	<%=Bean.getWebPosMenuHTML(pageFormName) %>
<% if (!Bean.hasMenuPermission(pageFormName, Bean.C_READ_MENU_PERMISSION, Bean.loginTerm)) { %>
	<%=Bean.getErrorPermissionMessage(pageFormName, Bean.loginTerm) %>
<% } else { %>

				
	<% if (tabCount > 0) { %>
	<table <%=Bean.getTableDetail2Param() %>>
		<tr>
			<td>
		<%

		String
			date_beg	= Bean.getDecodeParamPrepare(parameters.get("date_beg")),
			date_end	= Bean.getDecodeParamPrepare(parameters.get("date_end"));
		
		if (!(date_beg==null)) {
			Bean.setFormParam(pageFormName+"_DATE_FROM", date_beg, Bean.getSysDate());
			Bean.isOperationPeriodSet = true;
		}
		if (!(date_end==null)) {
			Bean.setFormParam(pageFormName+"_DATE_TO", date_end, Bean.getSysDate());
			Bean.isOperationPeriodSet = true;
		}
	
		String operFrom = Bean.getFormParam(pageFormName+"_DATE_FROM", Bean.getSysDate()).trim();
		String operTo = Bean.getFormParam(pageFormName+"_DATE_TO", Bean.getSysDate()).trim();
		String titleFromTo = "";
		if (!Bean.isEmpty(operFrom) && !Bean.isEmpty(operTo)) {
			titleFromTo = Bean.webposXML.getfieldTransl("title_operation_from_to", false);
			titleFromTo = titleFromTo.replace("$from$", operFrom).replace("$to$", operTo);
		} else {
			if (!Bean.isEmpty(operFrom)) {
				titleFromTo = Bean.webposXML.getfieldTransl("title_operations_from", false) + " " + operFrom;
			} else {
				if (!Bean.isEmpty(operTo)) {
					titleFromTo = Bean.webposXML.getfieldTransl("title_operations_to", false) + " " + operTo;
				} else {
					titleFromTo = "";
				}
			}
		}
		if (!Bean.isEmpty(rrn_find)) {
			titleFromTo = titleFromTo + "<br>" + Bean.webposXML.getfieldTransl("operation_find_rrn", false) + ": " + rrn_find;
		}
		if (!Bean.isEmpty(cd_card1_find)) {
			titleFromTo = titleFromTo + "<br>" + Bean.webposXML.getfieldTransl("title_questionnaire_cd_card", false) + ": " + cd_card1_find;
		}
		if (!Bean.isEmpty(operation_type)) {
			titleFromTo = titleFromTo + "<br>" + Bean.webposXML.getfieldTransl("operation_type", false) + ": " +  Bean.getWebposTransactionTypeName(operation_type).toLowerCase();
		}
		String payTypeName = "";
		if (!Bean.isEmpty(pay_type)) {
			payTypeName = Bean.getTransPayTypeName(pay_type);
			titleFromTo = titleFromTo + "<br>" + Bean.webposXML.getfieldTransl("goods_pay_way", false) + ": " + payTypeName.toLowerCase();
		}
		if (!Bean.isEmpty(operation_state)) {
			titleFromTo = titleFromTo + "<br>" + Bean.webposXML.getfieldTransl("operation_state", false) + ": " + Bean.getWebposTelegramStateName(operation_state).toLowerCase();
		}
		


		String
			inv_date_beg	= Bean.getDecodeParamPrepare(parameters.get("inv_date_beg")),
			inv_date_end	= Bean.getDecodeParamPrepare(parameters.get("inv_date_end"));
		String titleInvoiceFromTo = "";
		
		if (!(inv_date_beg==null)) {
			Bean.setFormParam(pageFormName+"_INV_DATE_FROM", inv_date_beg, Bean.getSysDate());
			Bean.isInvoicePeriodSet = true;
		}
		if (!(inv_date_end==null)) {
			Bean.setFormParam(pageFormName+"_INV_DATE_TO", inv_date_end, Bean.getSysDate());
			Bean.isInvoicePeriodSet = true;
		}
	
		String invFrom = Bean.getFormParam(pageFormName+"_INV_DATE_FROM", Bean.getSysDate()).trim();
		String invTo = Bean.getFormParam(pageFormName+"_INV_DATE_TO", Bean.getSysDate()).trim();
		if (!Bean.isEmpty(invFrom) && !Bean.isEmpty(invTo)) {
			titleInvoiceFromTo = Bean.webposXML.getfieldTransl("title_invoices_from_to", false);
			titleInvoiceFromTo = titleInvoiceFromTo.replace("$from$", invFrom).replace("$to$", invTo);
		} else {
			if (!Bean.isEmpty(invFrom)) {
				titleInvoiceFromTo = Bean.webposXML.getfieldTransl("title_invoices_from", false) + " " + invFrom;
			} else {
				if (!Bean.isEmpty(invTo)) {
					titleInvoiceFromTo = Bean.webposXML.getfieldTransl("title_invoices_to", false) + " " + invTo;
				} else {
					titleInvoiceFromTo = "";
				}
			}
		}
		
		if ("invupdate".equalsIgnoreCase(action)) {
	
			String[] results = new String[2];
			
			ArrayList<String> pParam = new ArrayList<String>();
			
			pParam.add(id_invoice);
			pParam.add(number_invoice);
			pParam.add(date_invoice);
			pParam.add(cd_fn_priority);
			pParam.add(paid_sum);
			pParam.add(LUD);
			pParam.add(Bean.getDateFormat());
			
			results 		= Bean.executeFunction("PACK$WEBPOS_UI.update_invoice", pParam, results.length);
			resultInt 		= results[0];
	 		resultMessage 	= results[1];
	 		
	 		action 			= "invdetail";
	 		
	 		if (Bean.C_SUCCESS_RESULT.equalsIgnoreCase(resultInt)) {
	 			if (Bean.isEmpty(resultMessage)) {
	 				resultMessage = "Параметры счета изменены";
	 			}
	 		}
		} else if ("invcancel".equalsIgnoreCase(action)) {
	
			String[] results = new String[2];
			
			ArrayList<String> pParam = new ArrayList<String>();
			
			pParam.add(id_invoice);
			pParam.add(LUD);
			
			results 		= Bean.executeFunction("PACK$WEBPOS_UI.cancel_invoice", pParam, results.length);
			resultInt 		= results[0];
	 		resultMessage 	= results[1];
	 		
	 		action 			= "invdetail";
	 		
	 		if (Bean.C_SUCCESS_RESULT.equalsIgnoreCase(resultInt)) {
	 			resultMessage = "Счет отменен";
	 		}
		} else if ("invcreate".equalsIgnoreCase(action)) {
	
			String[] results = new String[3];
			
			ArrayList<String> pParam = new ArrayList<String>();
			
			pParam.add(Bean.getCurrentTerm());
			pParam.add(number_invoice);
			pParam.add(create_automatic);
			pParam.add(date_invoice);
			pParam.add(cd_fn_priority);
			pParam.add(begin_period_date);
			pParam.add(end_period_date);
			pParam.add(Bean.getDateFormat());
			
			results 		= Bean.executeFunction("PACK$WEBPOS_UI.create_invoice", pParam, results.length);
			resultInt 		= results[0];
			id_invoice      = results[1];
	 		resultMessage 	= results[2];
	 		
	 		if (Bean.C_SUCCESS_RESULT.equalsIgnoreCase(resultInt)) {
		 		action 			= "invdetail";
	 		} else {
	 			action 			= "invnew";
	 		}
		}

		//titleInvoiceFromTo
	%>
	<div id="div_action_big">
				<h1>
				<%if ("1".equalsIgnoreCase(tab)) {%>
				<%=Bean.webposXML.getfieldTransl("title_operation", false) %>
				<% } %>
				<%if ("2".equalsIgnoreCase(tab)) {%>
				<%=Bean.webposXML.getfieldTransl("title_reports", false) %>
				<% } %>
				<%if ("3".equalsIgnoreCase(tab)) {%>
				<%=Bean.webposXML.getfieldTransl("title_invoices", false) %>
				<% } %>
				<%=Bean.getHelpButton("operation", "div_action_big") %></h1>
	<% if (tabCount>1) { %>
	<table <%=Bean.getTableDetail2Param()%>>
		<tr>
			<td>
				<div id="slidetabsmenu">
				<ul>
				<% if (Bean.hasReadMenuPermission("WEBPOS_SERVICE_OPERATION_OPERATIONS")) { %>
				<li <%if ("1".equalsIgnoreCase(tab)) {%> class="current"<% } %>><a href="#"><span onclick="ajaxpage('report/operation.jsp?tab=1', 'div_main')"><%=Bean.webposXML.getfieldTransl("title_operation", false) %></span></a></li>
				<% } %>
				<% if (Bean.hasReadMenuPermission("WEBPOS_SERVICE_OPERATION_REPORTS")) { %>
				<li <%if ("2".equalsIgnoreCase(tab)) {%> class="current"<% } %>><a href="#"><span onclick="ajaxpage('report/operation.jsp?tab=2', 'div_main')"><%=Bean.webposXML.getfieldTransl("title_reports", false) %></span></a></li>
				<% } %>
				<% if (Bean.hasReadMenuPermission("WEBPOS_SERVICE_OPERATION_INVOICES")) { %>
				<li <%if ("3".equalsIgnoreCase(tab)) {%> class="current"<% } %>><a href="#"><span onclick="ajaxpage('report/operation.jsp?tab=3', 'div_main')"><%=Bean.webposXML.getfieldTransl("title_invoices", false) %></span></a></li>
				<% } %>
				</ul>
				</div>
				<hr>
			</td>
			<% if ("3".equalsIgnoreCase(tab) && Bean.hasReadMenuPermission("WEBPOS_SERVICE_OPERATION_INVOICES") && !"invnew".equalsIgnoreCase(action)) { %>
			<td style="float:right">
				<button type="button" onclick="ajaxpage('report/operation.jsp?action=invnew','div_main');" class="button" style="height:22px; margin: 0 auto;" id="invnew_button">Новый счет</button>
			</td>
			<% } %>
		</tr>
	</table>
	<% } %>

<% if ("1".equalsIgnoreCase(tab) && Bean.hasReadMenuPermission("WEBPOS_SERVICE_OPERATION_OPERATIONS")) { %>
	
	<% if ("param".equalsIgnoreCase(action)) { %>
				<form name="updateForm1" id="updateForm1" accept-charset="UTF-8" method="POST">
			        <input type="hidden" name="action" value="show">
					<input type="hidden" name="transaction_page" value="1">
					<table class="action_table">
			  			<tr><td colspan="2">&nbsp;</td></tr>
						<tr><td <%=Bean.getDetailTableFirstColumnWidth() %>><%=Bean.webposXML.getfieldTransl("storno_rrn", false) %></td><td><input type="text" name="rrn_find" id="rrn_find" size="20" value="<%=rrn_find %>" class="inputfield"></td></tr>
              			<tr><td><%=Bean.webposXML.getfieldTransl("title_operations_from", false) %></td><td><%=Bean.getCalendarInputField("date_beg", operFrom, "10") %></td></tr>
              			<tr><td><%=Bean.webposXML.getfieldTransl("title_operations_to", false) %></td><td><%=Bean.getCalendarInputField("date_end", operTo, "10") %></td></tr>
						<tr><td><%=Bean.webposXML.getfieldTransl("title_operations_cd_card1", false) %></td><td><input type="text" name="cd_card1_find" id="cd_card1_find" size="20" value="<%=cd_card1_find %>" class="inputfield"></td></tr>
						<tr>
							<td><%=Bean.transactionXML.getfieldTransl("name_trans_type", false) %></td>
							<td><%=Bean.getSelectBeginHTML("operation_type", Bean.webposXML.getfieldTransl("operation_type", false)) %>
							 	<%= Bean.getWebposTransactionTypeOptions(operation_type, true) %>
							<%=Bean.getSelectOnChangeEndHTML() %></td>
						</tr>
						<tr>
							<td><%=Bean.webposXML.getfieldTransl("goods_pay_way", false) %></td>
							<td><%=Bean.getSelectBeginHTML("pay_type", Bean.webposXML.getfieldTransl("goods_pay_way", false)) %>
							 	<%=Bean.getSelectOptionHTML(pay_type, "", "") %>
							 	<%=Bean.getTransPayTypeOptions(pay_type, false) %>
							<%=Bean.getSelectOnChangeEndHTML() %></td>
						</tr>
						<tr>
							<td><%=Bean.transactionXML.getfieldTransl("name_trans_state", false) %></td>
							<td><%=Bean.getSelectBeginHTML("operation_state", Bean.webposXML.getfieldTransl("operation_state", false)) %>
							 	<%= Bean.getWebposTelegramStateOptions(operation_state, true) %>
							<%=Bean.getSelectOnChangeEndHTML() %></td>
						</tr>
			  			<tr><td colspan="2"  align="center">
			  				<%=Bean.getSubmitButtonAjax("report/operation.jsp", "find", "updateForm1", "div_main") %> 
							<% if (Bean.isOperationPeriodSet) { %> 
							<%=Bean.getSubmitButtonAjax("report/operation.jsp", "button_back", "updateFormBack", "div_main") %>
							<% } %>
						</td></tr>
					</table>
				</form>
	<form class="hiddenForm" name="updateFormBack" id="updateFormBack" accept-charset="UTF-8" method="POST">
		<input type="hidden" name="action" value="show">
	</form>
	<br>
		<%= Bean.getCalendarScript("date_beg", false) %>
		<%= Bean.getCalendarScript("date_end", false) %>
	<% } else if ("show".equalsIgnoreCase(action)) {
		StringBuilder html = new StringBuilder();
		html.append(Bean.loginTerm.getOnlineOperationHTML(Bean.getLoginUserId(), transaction_find, operation_type, operation_state, pay_type, rrn_find, cd_card1_find, operFrom, operTo, l_transaction_page_beg, l_transaction_page_end));
		%>
		<table <%=Bean.getTableBottomFilter() %>>
			<tr>
				<td colspan=10>
					<% if (!Bean.isEmpty(titleFromTo)) { %>
					<span style="font-size: 14px; color:red;font-weight:bold;"><%=Bean.webposXML.getfieldTransl("title_operation_filter_set", false) %></span>
					<span style="float:right;"><button class="button" id="change_button" onclick="ajaxpage('report/operation.jsp?' +  mySubmitForm('updateForm2'),'div_main');" type="button"><%=Bean.webposXML.getfieldTransl("title_button_filter_change", false) %></button></span><br>
					<span style="font-size: 12px; color:green;"><%=titleFromTo %></span>
					<% } else { %>
					<span style="font-size: 14px; color:blue;font-weight:bold;"><%=Bean.webposXML.getfieldTransl("title_operation_filter_not_set", false) %></span>
					<span style="float:right;"><button class="button" id="change_button" onclick="ajaxpage('report/operation.jsp?' +  mySubmitForm('updateForm2'),'div_main');" type="button"><%=Bean.webposXML.getfieldTransl("title_button_filter_change", false) %></button></span>
					<% } %>
					
				</td>
			</tr>
		  	<tr>
			<%= Bean.getFindHTML("transaction_find", transaction_find, "report/operation.jsp?action=show&transaction_page=1&", "div_main") %>
			
					
		    <!-- Вывод страниц -->
			<%= Bean.getPagesHTML(pageFormName + tagTransaction, "report/operation.jsp?action=show&", "transaction_page", Bean.loginTerm.getOperationsCount(), "div_main") %>
		  	</tr>
		</table>
		<%= html.toString()%>
		<form name="updateForm2" id="updateForm2" accept-charset="UTF-8" method="POST">
			<input type="hidden" name="action" value="param">
		</form>
	<% } else { %> 
		<%= Bean.getUnknownActionText(action) %><% 
	} %>
<% } %>
<% if ("2".equalsIgnoreCase(tab) && Bean.hasReadMenuPermission("WEBPOS_SERVICE_OPERATION_REPORTS")) { %>
	<% if ("param".equalsIgnoreCase(action)) { 
		StringBuilder html = new StringBuilder();
		html.append(Bean.loginUser.getWebPOSReportsHTML(report_find, l_report_page_beg, l_report_page_end, "report/operation.jsp", "div_main"));
		%>
			<table <%=Bean.getTableDetail2Param() %>>
				<tr>
					<td>
						<table <%=Bean.getTableBottomFilter() %>>
						  	<tr>
							<%= Bean.getFindHTML("report_find", report_find, "report/operation.jsp?report_page=1&", "div_main") %>
							
							
						    <!-- Вывод страниц -->
							<%= Bean.getPagesHTML(pageFormName + tagReport, "report/operation.jsp?", "report_page", Bean.loginUser.getReportsCount(), "div_main") %>
						  	</tr>
						</table>
					</td>
				</tr>
			</table>
			
				<%= html.toString()%>
				<br>
				<br>
	<% } else if ("show".equalsIgnoreCase(action)) {%>
	<%
		Connection temp_connection=(Connection)pageContext.getSession().getAttribute("CONNECTION");
	    // получить объект, который служит для дополнительных манипуляций с данными
	    ReportVariables report_variables=new ReportVariables(report_number,Bean.getSessionId(), Bean.getJSPDateFormat());
        	out.println("<form action=\"../reports/Reporter\" target=\"_report"+report_number+"\">");
        	out.println("<input type=\"hidden\" name=\"CASHIER_ID_DEALER\" value=\""+Bean.loginUser.getValue("ID_PARTNER_WORK")+"\">");
			out.println("<table " + Bean.getTableBottomFilter() + ">");
			out.println("<tr>");
			out.println("<td>" + report_variables.getReportNotes() + "</td>");
	        %>
			<script type="text/javascript">
	        function server_onload(){	        	
	        	<%=report_variables.getOnLoadJavaScript()%>
	        }
	        server_onload();
			set_width_for_all();
	        </script>
	        <%
	        
	        if ("WORKS".equalsIgnoreCase(report_variables.getReportState()) || 
	        		"OLD".equalsIgnoreCase(report_variables.getReportState())) {	        
		        // отобразить код всех переменных на форме
		        if(report_variables.getVariablesCount()>0){
		        	out.println("<tr>");
	        		out.println("<td colspan=\"2\" style=\"font-size: 16px; color:green; text-align: center;\"><b>"+report_variables.getReportName()+"</b></td>");
	        		out.println("</tr>");
	        		out.println("<tr>");
	        		out.println("<td>"+Bean.reportXML.getfieldTransl("ID_REPORT", false)+"</td>");
	        		out.println("<td valign=\"top\"><b>"+report_number+"</b></td>");
	        		out.println("</tr>");
		        	for(int counter=0;counter<report_variables.getVariablesCount();counter++){
		        		out.println("<tr>");
		        		out.println("<td>"+report_variables.getVariable(counter).getCaption()+"</td>");
		        		out.println("<td valign=\"top\">"+report_variables.getVariable(counter).getHTML()+"</td>");
		        		out.println("</tr>");
		        	}
		        	out.println("<tr>");
		        	out.println("<td>" + Bean.form_messageXML.getfieldTransl("REPORT_FORMAT", false) + "</td>");
		        	out.println("<td><select name=\"REPORT_FORMAT\" id=\"REPORT_FORMAT\" class=\"inputfield\">");
		        	out.println(Bean.getMeaningFromLookupNameOptions("REPORT_FORMAT", Bean.getReportFormat(), false));
		        	
		        	/*
		        	Utility utility=new Utility(con);
			        out.println(utility.getSelectBodyFromQuery("SELECT lookup_code, meaning FROM " + Bean.getGeneralDBScheme() + ". vc_lookup_values WHERE name_lookup_type = 'REPORT_FORMAT' AND enabled_flag = 'Y' ORDER BY meaning", Bean.loginUserParam.getValue("REPORT_FORMAT")));
			        Connector.closeConnection(con);
					*/
			        out.println("</select >");
		        	out.println("<input type=\"hidden\" name=\"REPORT_ID\" value=\""+report_number+"\">");	
		        	out.println("</td></tr>");
		        	out.println("<tr>");
		        	out.println("<td colspan=\"2\" align=center><input type=\"submit\" class=\"button\" value=\"" + Bean.buttonXML.getfieldTransl("show", false) + "\">");
		        	out.println("&nbsp;" + Bean.getGoBackButton("report/operation.jsp?action=param") + "</td>");
		        }
	        	out.println("</tr>");
	        	out.println("</table>");
	        	out.println("</form>");
	        	//out.println("</tr>");
	        	//out.println("</table>");
		        // добавить дополнительный код JavaScript на страницу
		        for(int counter=0;counter<report_variables.getVariablesCount();counter++){
		        	out.println(report_variables.getVariable(counter).getAdditionJavaScript());
		        }
	        } else {
	        	out.println("<tr>");
        		out.println("<td colspan=\"2\" style=\"font-size: 16px; color:green; text-align: center;\"><b>"+report_variables.getReportName()+"</b></td>");
        		out.println("</tr>");
        		out.println("<tr>");
        		out.println("<td>"+Bean.reportXML.getfieldTransl("ID_REPORT", false)+"</td>");
        		out.println("<td valign=\"top\"><b>"+report_number+"</b></td>");
        		out.println("</tr>");
        		out.println("<tr>");
        		out.println("<td colspan=\"2\" style=\"font-weight:bold; color:red; font-size:14px;\">"+Bean.reportXML.getfieldTransl("title_report_in_development", false)+"</td>");
        		out.println("</tr>");
	        	out.println("<tr>");
	        	out.println("<td colspan=\"2\" align=center>" + Bean.getGoBackButton("report/operation.jsp?action=param") + "</td>");
	        	out.println("</tr>");
	        	out.println("</table>");
 	        }
%>

	<% } %>
<% } %>
<% if ("3".equalsIgnoreCase(tab) && Bean.hasReadMenuPermission("WEBPOS_SERVICE_OPERATION_INVOICES")) { %>
	
	<% if ("invparam".equalsIgnoreCase(action)) { %>
				<form name="updateForm1" id="updateForm1" accept-charset="UTF-8" method="POST">
			        <input type="hidden" name="action" value="invshow">
					<input type="hidden" name="invoice_page" value="1">
					<table class="action_table">
			  			<tr><td colspan="2">&nbsp;</td></tr>
						<tr><td <%=Bean.getDetailTableFirstColumnWidth() %>><%=Bean.webposXML.getfieldTransl("title_invoices_from", false) %></td><td><%=Bean.getCalendarInputField("inv_date_beg", invFrom, "10") %></td></tr>
              			<tr><td><%=Bean.webposXML.getfieldTransl("title_invoices_to", false) %></td><td><%=Bean.getCalendarInputField("inv_date_end", invTo, "10") %></td></tr>
			  			<tr><td colspan="2"  align="center">
			  				<%=Bean.getSubmitButtonAjax("report/operation.jsp", "find", "updateForm1", "div_main") %> 
							<% if (Bean.isInvoicePeriodSet) { %> 
							<%=Bean.getSubmitButtonAjax("report/operation.jsp", "button_back", "updateFormBack", "div_main") %>
							<% } %>
						</td></tr>
					</table>
				</form>
	<form class="hiddenForm" name="updateFormBack" id="updateFormBack" accept-charset="UTF-8" method="POST">
		<input type="hidden" name="action" value="invshow">
	</form>
	<br>
		<%= Bean.getCalendarScript("inv_date_beg", false) %>
		<%= Bean.getCalendarScript("inv_date_end", false) %>
	<% } else if ("invshow".equalsIgnoreCase(action)) {
		StringBuilder html = new StringBuilder();
		html.append(Bean.loginTerm.getInvoicesHTML(invoice_find, invFrom, invTo, l_invoice_page_beg, l_invoice_page_end));
		%>
		<table <%=Bean.getTableBottomFilter() %>>
			<tr>
				<td colspan=10>
					<% if (!Bean.isEmpty(titleInvoiceFromTo)) { %>
					<span style="font-size: 14px; color:red;font-weight:bold;"><%=Bean.webposXML.getfieldTransl("title_operation_filter_set", false) %></span>
					<span style="float:right;"><button class="button" id="change_button" onclick="ajaxpage('report/operation.jsp?' +  mySubmitForm('updateForm2'),'div_main');" type="button"><%=Bean.webposXML.getfieldTransl("title_button_filter_change", false) %></button></span><br>
					<span style="font-size: 12px; color:green;"><%=titleInvoiceFromTo %></span>
					<% } else { %>
					<span style="font-size: 14px; color:blue;font-weight:bold;"><%=Bean.webposXML.getfieldTransl("title_operation_filter_not_set", false) %></span>
					<span style="float:right;"><button class="button" id="change_button" onclick="ajaxpage('report/operation.jsp?' +  mySubmitForm('updateForm2'),'div_main');" type="button"><%=Bean.webposXML.getfieldTransl("title_button_filter_change", false) %></button></span>
					<% } %>
				</td>
			</tr>
		  	<tr>
			<%= Bean.getFindHTML("invoice_find", invoice_find, "report/operation.jsp?action=invshow&invoice_page=1&", "div_main") %>
			
					
		    <!-- Вывод страниц -->
			<%= Bean.getPagesHTML(pageFormName + tagInvoice, "report/operation.jsp?action=invshow&", "invoice_page", Bean.loginTerm.getOperationsCount(), "div_main") %>
		  	</tr>
		</table>
		<%= html.toString()%>
		<form name="updateForm2" id="updateForm2" accept-charset="UTF-8" method="POST">
			<input type="hidden" name="action" value="invparam">
		</form>

	<% } else if ("invnew".equalsIgnoreCase(action)) { 
	
		if (Bean.isEmpty(date_invoice)) {
			date_invoice = Bean.getSysDate();
		}
		if (Bean.isEmpty(cd_fn_priority)) {
			cd_fn_priority = "MEDIUM";
		}
		
		%>
		<script type="text/javascript">
		function validateData(){
			var formData = new Array (
				new Array ('date_invoice', 'varchar2', 1),
				new Array ('cd_fn_priority', 'varchar2', 1)
			);
			return validateFormForID(formData, 'updateForm1');
		}
		</script>
	<form name="updateForm1" id="updateForm1" accept-charset="UTF-8" method="POST">
	    <input type="hidden" name="action" value="invcreate">
	    <input type="hidden" name="id_term" value="<%=Bean.getCurrentTerm() %>">
		<table <%=Bean.getTableBottomFilter() %>>
			<tr>
				<td colspan="2" style="font-size:14px;font-weight: bold; color:green;"><br><center><%= Bean.clearingXML.getfieldTransl("h_create_invoice", false) %></center></td>
			</tr>
			<tr>
				<td <%=Bean.getDetailTableFirstColumnWidth() %>><%= Bean.clearingXML.getfieldTransl("number_invoice", false) %> </td><td><input type="text" name="number_invoice" size="20" value="<%=number_invoice %>" class="inputfield"><!-- <br>
				<%=Bean.getCheckBoxBase("create_automatic", create_automatic, "Создать автоматически", "color:red;", "checkAutomatic()", false, false) %> --></td>
			</tr>
			<tr>
				<td><%= Bean.clearingXML.getfieldTransl("date_invoice", true) %></td> <td><%=Bean.getCalendarInputField("date_invoice", date_invoice, "10") %></td>
			</tr>
			<tr>
				<td><%= Bean.clearingXML.getfieldTransl("name_fn_priority", true) %></td> <td><select name="cd_fn_priority" class="inputfield"><%= Bean.getFinancePriorityOptions(cd_fn_priority, true) %></select></td>
			</tr>
			<tr>
				<td><%= Bean.clearingXML.getfieldTransl("begin_period_date", false) %></td> <td><%=Bean.getCalendarInputField("begin_period_date", begin_period_date, "10") %></td>
			</tr>
			<tr>
				<td><%= Bean.clearingXML.getfieldTransl("end_period_date", false) %></td> <td><%=Bean.getCalendarInputField("end_period_date", end_period_date, "10") %></td>
			</tr>
			<% if (!Bean.isEmpty(resultMessage)) { %>
				<% if (!(Bean.C_SUCCESS_RESULT.equalsIgnoreCase(resultInt))) { %>
				<tr><td colspan="2">&nbsp;</td></tr>
				<tr><td colspan="2"><span id="error_title"><%=Bean.webposXML.getfieldTransl("operation_error", false) %></span></td></tr>
				<tr><td colspan="2"><span id="error_description"><%=resultMessage %></span></td></tr>
				<% } else { %>
				<tr><td colspan="2">&nbsp;</td></tr>
				<tr><td colspan="2"><span id="confirm_description"><%=resultMessage %></span></td></tr>
				<% } %>
			<% } %>
				<tr><td colspan="2">&nbsp;</td></tr>
			<tr><td colspan="2"  align="center">
				<%=Bean.getSubmitButtonAjax("report/operation.jsp", "create", "updateForm1", "div_main", "validateData") %> 
				<%=Bean.getSubmitButtonAjax("report/operation.jsp", "button_back", "updateFormBack", "div_main") %>
			</td></tr>
		</table>
	</form>
		<%= Bean.getCalendarScript("date_invoice", false) %>
		<%= Bean.getCalendarScript("begin_period_date", false) %>
		<%= Bean.getCalendarScript("end_period_date", false) %>

	<form class="hiddenForm" name="updateFormBack" id="updateFormBack" accept-charset="UTF-8" method="POST">
		<input type="hidden" name="action" value="invshow">
	</form>

	<% } else if ("invdetail".equalsIgnoreCase(action)) {

		wpFNInvoiceObject invoice = new wpFNInvoiceObject(id_invoice);
		
		boolean isInvoceCancelled = "CANCELLED".equalsIgnoreCase(invoice.getValue("CD_FN_INVOICE_STATE"));
		boolean isInvoiceIsntPaid = "ISNT_PAID".equalsIgnoreCase(invoice.getValue("CD_FN_INVOICE_STATE"));
		%>
		<script type="text/javascript">
		function validateData(){
			var formData = new Array (
				new Array ('date_invoice', 'varchar2', 1),
				new Array ('cd_fn_priority', 'varchar2', 1),
				new Array ('paid_sum', 'oper_sum_zero', 0)
			);
			return validateFormForID(formData, 'updateForm1');
		}
		function confirmCancel(){
			var msg='Отменить счет?';
			var res=window.confirm(msg); 
			if (res) {
				return true;
			}
			return false;
		}
		</script>
	<form name="updateForm1" id="updateForm1" accept-charset="UTF-8" method="POST">
	    <input type="hidden" name="action" value="invupdate">
	    <input type="hidden" name="id_invoice" value="<%=id_invoice %>">
	    <input type="hidden" name="LUD" value="<%=invoice.getValue("LUD") %>">
		<table <%=Bean.getTableBottomFilter() %>>
			<tr>
				<td colspan="2" style="font-size:14px;font-weight: bold; color:green;"><br><center><div style="cursor:pointer;" onclick="ajaxpage('report/operation.jsp?tab=3&amp;id_term=<%=Bean.getCurrentTerm() %>&amp;id_invoice=<%=id_invoice %>&amp;action=invdetail', 'div_main')"><%= Bean.clearingXML.getfieldTransl("h_invoice_param", false) %></div></center>
				</td>
			</tr>
			<% if (isInvoceCancelled) { %>
			<tr>
				<td <%=Bean.getDetailTableFirstColumnWidth() %>><%= Bean.clearingXML.getfieldTransl("number_invoice", false) %> </td><td><input type="text" name="number_invoice" size="20" value="<%= invoice.getValue("NUMBER_INVOICE") %>" readonly="readonly" class="inputfield-ro"></td>
			</tr>
			<tr>
				<td><%= Bean.clearingXML.getfieldTransl("date_invoice", false) %></td> <td><input type="text" name="date_invoice" size="20" value="<%= invoice.getValue("DATE_INVOICE_DF") %>" readonly="readonly" class="inputfield-ro"></td>
			</tr>
			<tr>
				<td><%= Bean.clearingXML.getfieldTransl("name_fn_priority", false) %></td> <td><input type="text" name="name_fn_priority" size="20" value="<%= invoice.getValue("NAME_FN_PRIORITY") %>" readonly="readonly" class="inputfield-ro"></td>
			</tr>
			<% } else { %>
			<tr>
				<td <%=Bean.getDetailTableFirstColumnWidth() %>><%= Bean.clearingXML.getfieldTransl("number_invoice", false) %> </td><td><input type="text" name="number_invoice" size="20" value="<%= invoice.getValue("NUMBER_INVOICE") %>" class="inputfield"></td>
			</tr>
			<tr>
				<td><%= Bean.clearingXML.getfieldTransl("date_invoice", true) %></td> <td><%=Bean.getCalendarInputField("date_invoice", invoice.getValue("DATE_INVOICE_DF"), "10") %></td>
			</tr>
			<tr>
				<td><%= Bean.clearingXML.getfieldTransl("name_fn_priority", true) %></td> <td><select name="cd_fn_priority" class="inputfield"><%= Bean.getFinancePriorityOptions(invoice.getValue("CD_FN_PRIORITY"), true) %></select></td>
			</tr>
			<% } %>
			<tr>
				<td><%= Bean.clearingXML.getfieldTransl("sname_jur_prs_receiver", false) %></td><td><input type="text" name="sname_jur_prs_receiver" size="35" value="<%= invoice.getValue("SNAME_JUR_PRS_RECEIVER") %>, <%= invoice.getValue("ADR_JUR_PRS_RECEIVER") %>" title="<%= invoice.getValue("SNAME_JUR_PRS_RECEIVER") %>, <%= invoice.getValue("ADR_JUR_PRS_RECEIVER") %>" readonly="readonly" class="inputfield-ro"></td>			
			</tr>
			<tr>
				<td><%= Bean.clearingXML.getfieldTransl("sname_jur_prs_payer", false) %></td><td><input type="text" name="sname_jur_prs_payer" size="35" value="<%= invoice.getValue("SNAME_JUR_PRS_PAYER") %>, <%= invoice.getValue("ADR_JUR_PRS_PAYER") %>" title="<%= invoice.getValue("SNAME_JUR_PRS_PAYER") %>, <%= invoice.getValue("ADR_JUR_PRS_PAYER") %>" readonly="readonly" class="inputfield-ro"></td>			
			</tr>
			<tr>
				<td><%= Bean.clearingXML.getfieldTransl("begin_period_date", false) %></td> <td><input type="text" name="begin_period_date" size="20" value="<%= invoice.getValue("BEGIN_PERIOD_DATE_DF") %>" readonly="readonly" class="inputfield-ro"></td>
			</tr>
			<tr>
				<td><%= Bean.clearingXML.getfieldTransl("end_period_date", false) %></td> <td><input type="text" name="end_period_date" size="20" value="<%= invoice.getValue("END_PERIOD_DATE_DF") %>" readonly="readonly" class="inputfield-ro"></td>
			</tr>
			<tr><td colspan="2">
			<tr><td colspan="10"><b><%= Bean.clearingXML.getfieldTransl("h_invoice_lines", false) %></b></td></tr>
			<tr><td colspan="2">
				<div id="div_invoice_line">
					<table <%=Bean.getTableBottomFilter() %>>
						<tr>
						<%= Bean.getFindHTML("content_find", content_find, "report/operationupdate.jsp?action=invdetail&id_invoice=" + id_invoice + "&content_page=1&") %>
				
						<!-- Вывод страниц -->
						<%= Bean.getPagesHTML(pageFormName + tagContent, "report/operation.jsp?action=invdetail&id_invoice=" + id_invoice + "&tab="+Bean.currentMenu.getTabID("CRM_FINANCE_INVOICE_INFO")+"&", "content_page") %>
						<% if (Bean.hasWriteMenuPermission("WEBPOS_SERVICE_OPERATION_INVOICES") && isInvoiceIsntPaid) { %>
							<td width="20">
								<%=Bean.getAddButton("report/operationupdate.jsp?type=invline&process=no&action=add&id_invoice="+id_invoice, "div_invoice_line", "Добавить") %>
							</td>
						<% } %>
						</tr>
					</table>
					<%= invoice.getContentHTML(content_find, l_content_page_beg, l_content_page_end) %>
				</div>
			</td></tr>
			<tr><td colspan="2">&nbsp;</td></tr>
			<tr>
				<td><b><%= Bean.clearingXML.getfieldTransl("total_sum", false) %>, <%=invoice.getValue("SNAME_CURRENCY") %></b></td><td><input type="text" name="total_sum" size="20" value="<%= invoice.getValue("TOTAL_SUM_FRMT") %>" readonly="readonly" class="inputfield-ro"></td>
			</tr>
			<tr>
				<td><%= Bean.clearingXML.getfieldTransl("tax_sum", false) %>, <%=invoice.getValue("SNAME_CURRENCY") %> </td><td><input type="text" name="tax_sum" size="20" value="<%= invoice.getValue("TAX_SUM_FRMT") %>" readonly="readonly" class="inputfield-ro"></td>
			</tr>
			<tr>
				<td><%= Bean.clearingXML.getfieldTransl("total_without_tax_sum", false) %>, <%=invoice.getValue("SNAME_CURRENCY") %> </td><td><input type="text" name="total_without_tax_sum" size="20" value="<%= invoice.getValue("TOTAL_WITHOUT_TAX_SUM_FRMT") %>" readonly="readonly" class="inputfield-ro"></td>
			</tr>
			<tr>
				<td colspan="2" class="top_line_gray"><b>Информация об оплате</b></td>
			</tr>
			<tr>
				<td><%= Bean.clearingXML.getfieldTransl("name_fn_invoice_state", false) %></td> <td><input type="text" name="name_fn_invoice_state" size="20" value="<%= invoice.getValue("NAME_FN_INVOICE_STATE") %>" readonly="readonly" class="inputfield-ro"></td>
			</tr>
			<tr>
				<td><%= Bean.clearingXML.getfieldTransl("bank_account_receiver", false) %></td><td><input type="text" name="id_bank_account_receiver" size="35" value="<%= invoice.getValue("BANK_ACCOUNT_RECEIVER") %>" title="<%= invoice.getValue("BANK_ACCOUNT_RECEIVER") %>" readonly="readonly" class="inputfield-ro"></td>			
			</tr>
			<% if (isInvoceCancelled) { %>
			<tr>
				<td><span style="color:green;font-weight:bold;"><%= Bean.clearingXML.getfieldTransl("paid_sum", false) %>, <%=invoice.getValue("SNAME_CURRENCY") %></span></td><td><input type="text" name="paid_sum" size="20" value="<%= invoice.getValue("PAID_SUM_FRMT") %>" readonly="readonly" class="inputfield-ro"></td>
			</tr>
			<% } else { %>
			<tr>
				<td><span style="color:green;font-weight:bold;"><%= Bean.clearingXML.getfieldTransl("paid_sum", false) %>, <%=invoice.getValue("SNAME_CURRENCY") %></span></td><td><input type="text" name="paid_sum" size="20" value="<%= invoice.getValue("PAID_SUM_FRMT") %>" class="inputfield"></td>
			</tr>
			<% } %>
			<tr>
				<td><%= Bean.clearingXML.getfieldTransl("debt_sum", false) %>, <%=invoice.getValue("SNAME_CURRENCY") %></td><td><input type="text" name="debt_sum" size="20" value="<%= invoice.getValue("DEBT_SUM_FRMT") %>" readonly="readonly" class="inputfield-ro"></td>
			</tr>
			<tr>
				<td><%= Bean.clearingXML.getfieldTransl("overpayment_sum", false) %>, <%=invoice.getValue("SNAME_CURRENCY") %> </td><td><input type="text" name="overpayment_sum" size="20" value="<%= invoice.getValue("OVERPAYMENT_SUM_FRMT") %>" readonly="readonly" class="inputfield-ro"></td>
			</tr>
			<% if (!Bean.isEmpty(resultMessage)) { %>
				<% if (!(Bean.C_SUCCESS_RESULT.equalsIgnoreCase(resultInt))) { %>
				<tr><td colspan="2"><span id="error_title"><%=Bean.webposXML.getfieldTransl("operation_error", false) %></span></td></tr>
				<tr><td colspan="2"><span id="error_description"><%=resultMessage %></span></td></tr>
				<% } else { %>
				<tr><td colspan="2"><span id="confirm_description"><%=resultMessage %></span></td></tr>
				<% } %>
			<% } %>
			<tr><td colspan="2"  align="center">
				<% if (!isInvoceCancelled) { %>
				<%=Bean.getSubmitButtonAjax("report/operation.jsp", "button_save", "updateForm1", "div_main", "validateData") %> 
				<% if (isInvoiceIsntPaid) { %>
				<%=Bean.getSubmitButtonAjax("report/operation.jsp", "cancel", "updateFormCancel", "div_main", "confirmCancel") %>
				<% } %>
				<% } %> 
				<a style="margin:0 auto;" href="../reports/Reporter?&REPORT_ID=131&REPORT_FORMAT=HTML&NEED_DATE_IN_REPORT=Y&ID_INVOICE=<%=id_invoice %>" target="invoice<%=id_invoice %>"><span style="display:inline-block;height:27px;border-spacing: 0px;vertical-align: middle;"><span class="button" style="display:table-cell;vertical-align: middle;">Печатать</span></span></a>
				<%=Bean.getSubmitButtonAjax("report/operation.jsp", "button_back", "updateFormBack", "div_main") %>
			</td></tr>
		</table>
	</form>
	<% if (!isInvoceCancelled) { %>
		<%= Bean.getCalendarScript("date_invoice", false) %>
	<% } %>

	<form class="hiddenForm" name="updateFormCancel" id="updateFormCancel" accept-charset="UTF-8" method="POST">
		<input type="hidden" name="action" value="invcancel">
	    <input type="hidden" name="id_invoice" value="<%=id_invoice %>">
	    <input type="hidden" name="LUD" value="<%=invoice.getValue("LUD") %>">
	</form>
	<form class="hiddenForm" name="updateFormBack" id="updateFormBack" accept-charset="UTF-8" method="POST">
		<input type="hidden" name="action" value="invshow">
	</form>
	<% } else { %> 
		<%= Bean.getUnknownActionText(action) %><% 
	} %>
<% } %>

				</div>
			</td>
		</tr>
	</table>


	<% } else { %>
	<table <%=Bean.getTableDetail2Param() %>>
		<tr>
			<td>
				<div id="div_action_big">
				<h1><%=Bean.webposXML.getfieldTransl("title_operation", false) %><%=Bean.getHelpButton("operation", "div_action_big") %></h1>
					<table class="action_table">
						<tr><td align="center" style="padding: 10px;"><font style="font-size: 22px; color:red; font-weight: bold;"><%=Bean.webposXML.getfieldTransl("operation_error", false) %></font></td></tr>
						<tr><td align="center">Доступ запрещен</td></tr>
						<tr><td align="center">&nbsp;</td></tr>
					</table>
				</div>
			</td>
		</tr>
	</table>

	<% } %>
<% } %>
</body>
</html>