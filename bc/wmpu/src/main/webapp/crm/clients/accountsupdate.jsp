<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcCRMBean"  />

<%= Bean.getLogOutScript(request) %>

<%@page import="bc.objects.bcBankAccountObject"%>

<%@page import="java.util.HashMap"%>
<%@page import="bc.objects.bcClubShortObject"%>
<%@page import="java.util.ArrayList"%><html>
<head>
	<%= Bean.getMetaContent() %>
	<%= Bean.getBottomFrameCSS() %>

</head>

<body>
<%
HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());

String pageFormName = "CLIENTS_BANK_ACCOUNTS";

Bean.setJspPageForTabName(pageFormName);

request.setCharacterEncoding("UTF-8");

String external_id		= Bean.getDecodeParam(parameters.get("id")); 
String id_bank_account	= Bean.getDecodeParamPrepare(parameters.get("id_bank_account")); 
String action			= Bean.getDecodeParam(parameters.get("action")); 
String process			= Bean.getDecodeParam(parameters.get("process"));
String back_type		= Bean.getDecodeParamPrepare(parameters.get("back_type"));
String id_jur_prs		= Bean.getDecodeParamPrepare(parameters.get("id_jur_prs")); 
String id_nat_prs		= Bean.getDecodeParamPrepare(parameters.get("id_nat_prs")); 

String updateLink = "../crm/clients/accountsupdate.jsp";
String backLink = "";
String generalLink = "";
back_type = Bean.isEmpty(back_type)?"ACCOUNT":back_type;
System.out.println("back_type="+back_type);
if ("ACCOUNT".equalsIgnoreCase(back_type)) {
	generalLink = "../crm/clients/accounts.jsp?";
	if (action.equalsIgnoreCase("add")) {
		backLink = "../crm/clients/accounts.jsp?";
	} else {
		backLink = "../crm/clients/accountspecs.jsp?id="+id_bank_account;
	}
} else if ("PARTNER".equalsIgnoreCase(back_type)) {
	generalLink = "../crm/clients/yurpersonspecs.jsp?id="+external_id;
	backLink = "../crm/clients/yurpersonspecs.jsp?id="+external_id;
} else if ("NAT_PRS".equalsIgnoreCase(back_type)) {
	generalLink = "../crm/clients/natpersonspecs.jsp?id="+external_id;
	backLink = "../crm/clients/natpersonspecs.jsp?id="+external_id;
}


if (process.equalsIgnoreCase("no"))
/* вибираємо тип дії (добавити, видалити...)*/
	{
	   /*  --- Добавити запис --- */
    	if (action.equalsIgnoreCase("add") || action.equalsIgnoreCase("add2")) {
    		
    		bcClubShortObject club = new bcClubShortObject(Bean.getCurrentClubID());
	        
	        %>
	<script>
		var formData = new Array (
			new Array ('name_entry', 'varchar2', 1),
			new Array ('cd_bank_account_type', 'varchar2', 1),
			new Array ('number_bank_account', 'varchar2', 1),
			new Array ('name_bank', 'varchar2', 1),
			new Array ('cd_currency', 'varchar2', 1),
			new Array ('name_club', 'varchar2', 1),
			new Array ('date_beg', 'varchar2', 1)
		);
		function myValidateForm() {
			return validateForm(formData);
		}
	</script> 

			<% if ("ACCOUNT".equalsIgnoreCase(back_type)) { %>
			<%= Bean.getOperationTitle(
					Bean.accountXML.getfieldTransl("h_accounts_add", false),
					"Y",
					"Y") 
			%>
			<% } else { %>
			<%= Bean.getOperationTitleShort(
					"",
					Bean.accountXML.getfieldTransl("h_accounts_add", false),
					"Y",
					"Y") 
			%>
			<% } %>
    <form action="<%=updateLink %>" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return validateForm(formData)">
	        <input type="hidden" name="action" value="add">
	        <input type="hidden" name="process" value="yes">
    		<input type="hidden" name="id" value="<%=external_id %>">
    		<input type="hidden" name="id_bank_account" value="<%=id_bank_account %>">
    		<input type="hidden" name="back_type" value="<%=back_type %>">
	<table <%=Bean.getTableDetailParam() %>>
		<tr>
			<td><%=Bean.accountXML.getfieldTransl("name_owner_bank_account", true)%>
			<% if ("NAT_PRS".equalsIgnoreCase(back_type)) { %>
				<%=Bean.getGoToNatPrsLink(id_nat_prs) %>
			<% } else if ("PARTNER".equalsIgnoreCase(back_type)) { %>
				<%=Bean.getGoToJurPrsHyperLink(id_jur_prs) %>
			<% } %>
			</td>
			<td>
				<%=Bean.getWindowFindJurAndNatPrs("PARTNER".equalsIgnoreCase(back_type)?id_jur_prs:("NAT_PRS".equalsIgnoreCase(back_type)?id_nat_prs:""), "ALL", "PARTNER".equalsIgnoreCase(back_type)?"JUR_PRS":"NAT_PRS", "50") %>
			</td>
 		    <td><%= Bean.clubXML.getfieldTransl("club", true) %>
				<%= Bean.getGoToClubLink(club.getValue("ID_CLUB")) %>
			</td>
		  	<td>
				<%=Bean.getWindowFindClub("club", club.getValue("ID_CLUB"), club.getValue("SNAME_CLUB"), "40") %>
		  	</td>		
		</tr>
		<tr>
			<td><%=Bean.accountXML.getfieldTransl("name_bank_account_type", true)%></td><td><select name="cd_bank_account_type"  class="inputfield"><%=Bean.getBankAccountTypeOptions("", true)%></select></td>
			<td><%=Bean.clubXML.getfieldTransl("club_date_beg", true)%></td> <td><%=Bean.getCalendarInputField("date_beg", "", "10") %></td>
		</tr>
		<tr>
			<td><%=Bean.accountXML.getfieldTransl("number_bank_account", true)%></td><td><input type="text" name="number_bank_account" size="60" value="" class="inputfield"></td>
			<td rowspan="3"><%=Bean.accountXML.getfieldTransl("desc_bank_account", false)%></td><td rowspan="3"><textarea name="desc_bank_account" cols="70" rows="3" class="inputfield"></textarea></td>
		</tr>
		<tr>
			<td><%=Bean.accountXML.getfieldTransl("name_bank_alt", true)%></td>
			<td>
				<%=Bean.getWindowFindJurPrs("bank", "", "BANK", "50") %>
			</td>
		</tr>
		<tr>
			<td><%=Bean.accountXML.getfieldTransl("name_currency", true)%></td> <td><select name="cd_currency"  class="inputfield"><%=Bean.getCurrencyOptions("", true)%></select> </td>
		</tr>
 		<tr>
			<td colspan="6" align="center">
				<%=Bean.getSubmitButtonAjax(updateLink, "submit", "updateForm", ("ACCOUNT".equalsIgnoreCase(back_type))?"div_main":"div_data_detail") %>
				<%=Bean.getResetButton() %>
				<%=Bean.getGoBackButton(backLink) %>
			</td>
		</tr>

	</table>
	</form>

	<!-- Скрипт для втавки меню вибору дати -->
		<%= Bean.getCalendarScript("date_beg", false) %>

	        <%
    	} else if (action.equalsIgnoreCase("edit")) {
    		bcBankAccountObject account = new bcBankAccountObject(id_bank_account);

    		%> 
    			<script>
    				var formData = new Array (
    					new Array ('name_entry', 'varchar2', 1),
    					new Array ('cd_bank_account_type', 'varchar2', 1),
    					new Array ('number_bank_account', 'varchar2', 1),
    					new Array ('name_bank', 'varchar2', 1),
    					new Array ('cd_currency', 'varchar2', 1),
    					new Array ('name_club', 'varchar2', 1),
    					new Array ('date_beg', 'varchar2', 1)
    				);
    				function myValidateForm() {
    					return validateForm(formData);
    				}
    			</script>
			
			<%= Bean.getOperationTitleShort(
					"",
					Bean.accountXML.getfieldTransl("h_accounts_edit", false),
					"Y",
					"Y") 
			%>
    			
    		    <form action="../crm/clients/accountsupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return validateForm(formData)">
    		        <input type="hidden" name="action" value="edit">
    		        <input type="hidden" name="process" value="yes">
    		        <input type="hidden" name="id_bank_account" value="<%=account.getValue("ID_BANK_ACCOUNT")%>">
    		<input type="hidden" name="id" value="<%=external_id %>">
    		<input type="hidden" name="back_type" value="<%=back_type %>">
    			<table <%=Bean.getTableDetailParam() %>>
    				<tr>
    					<% if (Bean.isEmpty(account.getValue("ID_NAT_PRS"))) { %>
    					<td><%=Bean.accountXML.getfieldTransl("name_owner_bank_account", true)%>
    						<%=Bean.getGoToJurPrsHyperLink(account.getValue("ID_JUR_PRS")) %>
    					</td>
    					<td>
    						<%=Bean.getWindowFindJurAndNatPrs(account.getValue("ID_JUR_PRS"), "ALL", "JUR_PRS", "37") %>
    					</td>
    					<% } else { %>
    					<td><%= Bean.accountXML.getfieldTransl("name_owner_bank_account", true) %>
    						<%= Bean.getGoToNatPrsLink(account.getValue("ID_NAT_PRS")) %>
    					</td>
    					<td>
    						<%=Bean.getWindowFindJurAndNatPrs(account.getValue("ID_NAT_PRS"), "ALL", "NAT_PRS", "37") %>
    					</td>
    					<% } %>
    		 		    <td><%= Bean.clubXML.getfieldTransl("club", false) %>
    						<%= Bean.getGoToClubLink(account.getValue("ID_CLUB")) %>
    					</td><td><input type="text" name="name_club" size="50" value="<%= Bean.getClubShortName(account.getValue("ID_CLUB")) %>" readonly="readonly" class="inputfield-ro"></td>
    				</tr>
    				<tr>		
    					<td><%=Bean.accountXML.getfieldTransl("name_bank_account_type", true)%></td><td><select name="cd_bank_account_type"  class="inputfield"><%=Bean.getBankAccountTypeOptions(account.getValue("CD_BANK_ACCOUNT_TYPE"), true)%></select></td>
    					<td><%=Bean.clubXML.getfieldTransl("club_date_beg", true)%></td> <td><%=Bean.getCalendarInputField("date_beg", account.getValue("DATE_BEG_FRMT"), "10") %></td>
    				</tr>
    				<tr>		
    					<td><%=Bean.accountXML.getfieldTransl("number_bank_account", true)%></td><td><input type="text" name="number_bank_account" size="50" value="<%=account.getValue("NUMBER_BANK_ACCOUNT")%>" class="inputfield"></td>
    					<td rowspan="3"><%=Bean.accountXML.getfieldTransl("desc_bank_account", false)%></td><td rowspan="3"><textarea name="desc_bank_account" cols="47" rows="3" class="inputfield"><%=account.getValue("DESC_BANK_ACCOUNT")%></textarea></td>
    				</tr>
    				<tr>
    					<td><%=Bean.accountXML.getfieldTransl("name_bank_alt", true)%>
    						<%= Bean.getGoToJurPrsHyperLink(account.getValue("ID_BANK")) %>
    					</td>
    					<td>
    						<%=Bean.getWindowFindJurPrs("bank", account.getValue("ID_BANK"), "BANK", "37") %>
    					</td>			
    				</tr>
    				<tr>
    					<td><%=Bean.accountXML.getfieldTransl("name_currency", true)%></td> <td><select name="cd_currency" class="inputfield"><%=Bean.getCurrencyOptions(account.getValue("CD_CURRENCY"), true)%></select> </td>
    				</tr>
    				<%=	Bean.getIdCreationAndMoficationRecordFields(
    						account.getValue("ID_BANK_ACCOUNT"),
    						account.getValue(Bean.getCreationDateFieldName()),
    						account.getValue("CREATED_BY"),
    						account.getValue(Bean.getLastUpdateDateFieldName()),
    						account.getValue("LAST_UPDATE_BY")
    					) %>

    				<tr>
    					<td colspan="6" align="center">
    						<%=Bean.getSubmitButtonAjax(updateLink, "submit", "updateForm", "div_data_detail") %>
    						<%=Bean.getResetButton() %>
    						<%=Bean.getGoBackButton(backLink) %>
    					</td>
    				</tr>

    			</table>
    			</form>
    			
    			<!-- Скрипт для втавки меню вибору дати -->
    			<%= Bean.getCalendarScript("date_beg", false) %>

    		<% 
    	} else if (action.equalsIgnoreCase("calc_rests") || action.equalsIgnoreCase("calc_rests_all")) { 

    		bcClubShortObject club = null;
    		bcBankAccountObject account = null;
    		String id_club = "";
    		String sname_club = "";
    		if (!(id_bank_account==null || "".equalsIgnoreCase(id_bank_account))) {
    			account = new bcBankAccountObject(id_bank_account);
    			id_club = account.getValue("ID_CLUB");
    			sname_club = Bean.getClubShortName(account.getValue("ID_CLUB"));
    		} else {
    			club = new bcClubShortObject(Bean.getCurrentClubID());
    			id_club = club.getValue("ID_CLUB");
    			sname_club = club.getValue("SNAME_CLUB");
    		}
    		
    		if (action.equalsIgnoreCase("calc_rests_all")) { 
    		%>
    			<%= Bean.getOperationTitle(
    					Bean.accountXML.getfieldTransl("h_calc_rests", false),
    					"N",
    					"N") 
    			%>
			<% } else { %>
    			<%= Bean.getOperationTitleShort(
    					"",
    					Bean.accountXML.getfieldTransl("h_calc_rests", false),
    					"N",
    					"N") 
    			%>
			<% } %>
    		<form action="../crm/clients/accountsupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return validateForm(formData)">
    		    <input type="hidden" name="action" value="calc_rests">
    		    <input type="hidden" name="process" value="yes">
    		    <input type="hidden" name="id_bank_account" value="<%=id_bank_account %>">
    		<table <%=Bean.getTableDetailParam() %>>
				<% if (!(id_bank_account==null || "".equalsIgnoreCase(id_bank_account))) { %>
				<tr>
					<td><%=Bean.accountXML.getfieldTransl("number_bank_account", false)%> </td>
					<td>
						<input type="hidden" name="id_bank_account" size="20" value="<%=id_bank_account%>">
						<input type="text" name="number_bank_account" size="40" value="<%=account.getValue("NUMBER_BANK_ACCOUNT")%>" readonly="readonly" class="inputfield-ro">
					</td>
				</tr>
				<% } %>
    			<tr>
    				<td><%= Bean.clubXML.getfieldTransl("club", false) %>
    				<%= Bean.getGoToClubLink(id_club) %>
    			  	</td>
    			  	<td>
    					<%=Bean.getWindowFindClub("club", id_club, sname_club, "25") %>
    			  	</td>
    			</tr>
    			<tr>
    				<td colspan="6" align="center">
    					<%=Bean.getSubmitButtonAjax("../crm/clients/accountsupdate.jsp", "submit", "updateForm") %>
    					<%=Bean.getResetButton() %>
						<% String goBackHyperLink = "";
						   if (!(id_bank_account==null || "".equalsIgnoreCase(id_bank_account))) {
							   goBackHyperLink = "../crm/clients/accountspecs.jsp?id=" + id_bank_account;
						   } else {
							   goBackHyperLink = "../crm/clients/accounts.jsp";
						   }
						%>
    					<%=Bean.getGoBackButton(goBackHyperLink) %>
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
	    number_bank_account 		= Bean.getDecodeParamPrepare(parameters.get("number_bank_account")),
	    desc_bank_account 			= Bean.getDecodeParamPrepare(parameters.get("desc_bank_account")),
	    cd_currency 				= Bean.getDecodeParamPrepare(parameters.get("cd_currency")),
	    cd_bank_account_type 		= Bean.getDecodeParamPrepare(parameters.get("cd_bank_account_type")),
	    date_beg 					= Bean.getDecodeParamPrepare(parameters.get("date_beg")),
	    id_bank_branch 				= Bean.getDecodeParamPrepare(parameters.get("id_bank_branch")),
	    name_bank 					= Bean.getDecodeParamPrepare(parameters.get("name_bank")),
	    id_bank		 				= Bean.getDecodeParamPrepare(parameters.get("id_bank")),
	    code_country 				= Bean.getDecodeParamPrepare(parameters.get("code_country")),
	    name_jur_prs 				= Bean.getDecodeParamPrepare(parameters.get("name_jur_prs")),
	    contact_person 				= Bean.getDecodeParamPrepare(parameters.get("contact_person")),
	    contact_phone 				= Bean.getDecodeParamPrepare(parameters.get("contact_phone")),
	    id_club				 		= Bean.getDecodeParamPrepare(parameters.get("id_club")),
	    date_end_period				= Bean.getDecodeParamPrepare(parameters.get("date_end_period")),
	    debug					 	= Bean.getDecodeParamPrepare(parameters.get("debug"));

    if (action.equalsIgnoreCase("add")) { 
    	
		ArrayList<String> pParam = new ArrayList<String>();
	    	      				
    	pParam.add(id_bank);
    	pParam.add(cd_currency);
    	pParam.add(id_jur_prs);
    	pParam.add(id_nat_prs);
    	pParam.add(date_beg);
    	pParam.add(number_bank_account);
    	pParam.add(cd_bank_account_type);
    	pParam.add(desc_bank_account);
    	pParam.add(contact_person);
    	pParam.add(contact_phone);
    	pParam.add(id_club);
    	pParam.add(Bean.getDateFormat());
		
		%>
		<%= Bean.executeInsertFunction("PACK$BANK_ACCOUNT_UI.add_bank_account", pParam, backLink + "&id_bank_account=" , "") %>
		<% 	

   	} else if (action.equalsIgnoreCase("remove")) { 
   		
	   	ArrayList<String> pParam = new ArrayList<String>();
		 		 	    	      				
		pParam.add(id_bank_account);

		%>
		<%= Bean.executeDeleteFunction("PACK$BANK_ACCOUNT_UI.delete_bank_account", pParam, generalLink , "") %>
		<%

	} else if (action.equalsIgnoreCase("calc_rests")) { 
		
		ArrayList<String> pParam = new ArrayList<String>();
 		 	    	      				
 	    pParam.add(id_club);
	 	pParam.add(id_bank_account);
	 	
	 	%>
		<% String goHyperLink = "";
		   if (!(id_bank_account==null || "".equalsIgnoreCase(id_bank_account))) {
			   goHyperLink = "../crm/clients/accountspecs.jsp?id=" + id_bank_account;
		   } else {
			   goHyperLink = "../crm/clients/accounts.jsp";
		   }
		%>
		<%= Bean.executeFunction("UPDATE", "PACK$BANK_ACCOUNT_UI.calc_rests", pParam, goHyperLink , "") %>
		<% 	

	} else if (action.equalsIgnoreCase("edit")) { 
		
	 	ArrayList<String> pParam = new ArrayList<String>();
	 	    	      				
      	pParam.add(id_bank_account);
     	pParam.add(id_bank);
     	pParam.add(cd_currency);
     	pParam.add(id_jur_prs);
     	pParam.add(id_nat_prs);
     	pParam.add(date_beg);
     	pParam.add(number_bank_account);
     	pParam.add(cd_bank_account_type);
     	pParam.add(desc_bank_account);
     	pParam.add(contact_person);
     	pParam.add(contact_phone);
     	pParam.add(Bean.getDateFormat());
		
		%>
		<%= Bean.executeUpdateFunction("PACK$BANK_ACCOUNT_UI.update_bank_account", pParam, backLink, "") %>
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
