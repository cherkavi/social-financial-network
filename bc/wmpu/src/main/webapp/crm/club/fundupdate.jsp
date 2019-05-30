<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcCRMBean"  />

<%@page import="bc.objects.bcLoySchemeObject"%>
<%@page import="bc.objects.bcComissionObject"%>
<%@page import="bc.objects.bcClubRelationshipObject"%>
<%@page import="java.util.HashMap"%>
<%@page import="bc.objects.bcClubShortObject"%>

<%@page import="java.util.ArrayList"%>
<%@page import="bc.objects.bcClubFundObject"%><html>

<%= Bean.getLogOutScript(request) %>

<head>
	<%= Bean.getMetaContent() %>
	<%= Bean.getBottomFrameCSS() %>
</head>

<body>

<%

HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());

String pageFormName = "CLUB_FUND";

Bean.setJspPageForTabName(pageFormName);

request.setCharacterEncoding("UTF-8");

String external_id	= Bean.getDecodeParamPrepare(parameters.get("id"));
String id_club_fund	= Bean.getDecodeParamPrepare(parameters.get("id_club_fund")); 
String type			= Bean.getDecodeParamPrepare(parameters.get("type")); 
String action		= Bean.getDecodeParamPrepare(parameters.get("action")); 
String process		= Bean.getDecodeParamPrepare(parameters.get("process"));
String back_type	= Bean.getDecodeParamPrepare(parameters.get("back_type"));

String updateLink = "../crm/club/fundupdate.jsp";
String backLink = "";
String generalLink = "";
back_type = Bean.isEmpty(back_type)?"CLUB_FUND":back_type;
System.out.println("back_type="+back_type);
if ("CLUB_FUND".equalsIgnoreCase(back_type)) {
	generalLink = "../crm/club/fund.jsp?";
	if (action.equalsIgnoreCase("add")) {
		backLink = "../crm/club/fund.jsp?";
	} else {
		backLink = "../crm/club/fundspecs.jsp?id="+id_club_fund;
	}
} else if ("CLUB".equalsIgnoreCase(back_type)) {
	generalLink = "../crm/club/clubspecs.jsp?id="+external_id;
	backLink = "../crm/club/clubspecs.jsp?id="+external_id;
} else if ("PARTNER".equalsIgnoreCase(back_type)) {
	generalLink = "../crm/clients/yurpersonspecs.jsp?id="+external_id;
	backLink = "../crm/clients/yurpersonspecs.jsp?id="+external_id;
}


if (type.equalsIgnoreCase("general")) {
	if (process.equalsIgnoreCase("no")) {
		if (action.equalsIgnoreCase("add") || action.equalsIgnoreCase("add2")) {
    		
			bcClubShortObject club = new bcClubShortObject(Bean.getCurrentClubID());
			
	        %> 
			<% if ("CLUB_FUND".equalsIgnoreCase(back_type)) { %>
			<%= Bean.getOperationTitle(
					Bean.clubfundXML.getfieldTransl("h_add_club_fund", false),
					"Y",
					"N") 
			%>
			<% } else { %>
			<%= Bean.getOperationTitleShort(
					"",
					Bean.clubfundXML.getfieldTransl("h_add_club_fund", false),
					"Y",
					"N") 
			%>
			<% } %>
		<script>
			var formData1 = new Array (
				new Array ('name_jur_prs', 'varchar2', 1),
				new Array ('name_club_fund', 'varchar2', 1),
				new Array ('sname_club_fund', 'varchar2', 1),
				new Array ('name_club', 'varchar2', 1),
				new Array ('date_beg', 'varchar2', 1)
			);
			function myValidateForm() {
				return validateForm(formData1);
			}
		</script>

	<form action="../crm/club/fundupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return validateForm(formData)">  	        
	    <input type="hidden" name="type" value="general">
	    <input type="hidden" name="action" value="add">
	    <input type="hidden" name="process" value="yes">
    	<input type="hidden" name="id" value="<%=external_id %>">
    	<input type="hidden" name="back_type" value="<%=back_type %>">
		<table <%=Bean.getTableDetailParam() %>>
		<tr>
			<td><%= Bean.clubfundXML.getfieldTransl("sname_jur_prs", true) %></td>
			<td>
				<%=Bean.getWindowFindJurPrs("jur_prs", "", "ALL", "60") %>
			</td>			
 		    <td><%= Bean.clubXML.getfieldTransl("club", true) %>
				<%= Bean.getGoToClubLink(club.getValue("ID_CLUB")) %>
			</td>
		  	<td>
				<%=Bean.getWindowFindClub("club", club.getValue("ID_CLUB"), club.getValue("SNAME_CLUB"), "25") %>
		  	</td>
		</tr>
	    <tr>
			<td><%= Bean.clubfundXML.getfieldTransl("name_club_fund", true) %></td> <td><input type="text" name="name_club_fund" id="name_club_fund" size="70" value="" class="inputfield"></td>
			<td><%= Bean.clubfundXML.getfieldTransl("fund_date_beg", true) %></td> <td><%=Bean.getCalendarInputField("date_beg", Bean.getSysDate(), "10") %></td>
		</tr>
	    <tr>
			<td><%= Bean.clubfundXML.getfieldTransl("sname_club_fund", true) %></td> <td><input type="text" name="sname_club_fund" size="70" value="" class="inputfield"></td>
			<td><%= Bean.clubfundXML.getfieldTransl("fund_date_end", false) %></td> <td><%=Bean.getCalendarInputField("date_end", "", "10") %></td>
		</tr>
	    <tr>
			<td><%= Bean.clubfundXML.getfieldTransl("desc_club_fund", false) %></td> <td  colspan="3"><textarea name="desc_club_fund" cols="67" rows="3" class="inputfield"></textarea></td>
		</tr>
	 		<tr>
				<td colspan="6" align="center">
					<%=Bean.getSubmitButtonAjax(updateLink) %>
					<%=Bean.getResetButton() %>
					<%=Bean.getGoBackButton(backLink) %>
				</td>
			</tr>
		</table>
		</form>
	<%= Bean.getCalendarScript("date_beg", false) %>
	<%= Bean.getCalendarScript("date_end", false) %>

		<% 	
		} else if (action.equalsIgnoreCase("edit")) {
			bcClubFundObject fund = new bcClubFundObject(id_club_fund);%>
			
			<script>
				var formData = new Array (
					new Array ('name_club_fund', 'varchar2', 1),
					new Array ('sname_club_fund', 'varchar2', 1),
					new Array ('date_beg', 'varchar2', 1)
				);
				function myValidateForm() {
					return validateForm(formData);
				}
			</script>

			<%= Bean.getOperationTitleShort(
					"",
					Bean.clubfundXML.getfieldTransl("h_edit_club_fund", false),
					"Y",
					"N") 
			%>
	<div id="div_detail">
	<form action="../crm/club/fundupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST">
	    <input type="hidden" name="id_club_fund" value="<%=id_club_fund %>">
		<input type="hidden" name="LUD" value="<%=fund.getValue("LUD") %>">
		<input type="hidden" name="action" value="edit">
	    <input type="hidden" name="process" value="yes">
	    <input type="hidden" name="type" value="general">
    	<input type="hidden" name="id" value="<%=external_id %>">
    	<input type="hidden" name="back_type" value="<%=back_type %>">
		<table <%=Bean.getTableDetailParam() %>>
		    <tr>
				<td><%= Bean.clubfundXML.getfieldTransl("sname_jur_prs", false) %>
					<%= Bean.getGoToJurPrsHyperLink(fund.getValue("ID_JUR_PRS")) %>
				</td><td><input type="text" name="name_jur_prs" size="70" value="<%= fund.getValue("SNAME_JUR_PRS") %>" readonly="readonly" class="inputfield-ro"></td>
				<td><%= Bean.clubXML.getfieldTransl("club", false) %>
					<%= Bean.getGoToClubLink(fund.getValue("ID_CLUB")) %>
				</td><td><input type="text" name="name_club" size="30" value="<%= Bean.getClubShortName(fund.getValue("ID_CLUB")) %>" readonly="readonly" class="inputfield-ro"></td>
			</tr>
		    <tr>
				<td><%= Bean.clubfundXML.getfieldTransl("name_club_fund", true) %></td> <td><input type="text" name="name_club_fund" size="70" value="<%=fund.getValue("NAME_CLUB_FUND") %>" class="inputfield"></td>
				<td><%= Bean.clubfundXML.getfieldTransl("fund_date_beg", true) %></td> <td><%=Bean.getCalendarInputField("date_beg", fund.getValue("DATE_BEG_FRMT"), "10") %></td>
			</tr>
		    <tr>
				<td><%= Bean.clubfundXML.getfieldTransl("sname_club_fund", true) %></td> <td><input type="text" name="sname_club_fund" size="70" value="<%=fund.getValue("SNAME_CLUB_FUND") %>" class="inputfield"></td>
				<td><%= Bean.clubfundXML.getfieldTransl("fund_date_end", false) %></td> <td><%=Bean.getCalendarInputField("date_end", fund.getValue("DATE_END_FRMT"), "10") %></td>
			</tr>
		    <tr>
				<td><%= Bean.clubfundXML.getfieldTransl("desc_club_fund", false) %></td> <td><textarea name="desc_club_fund" cols="67" rows="3" class="inputfield"><%= fund.getValue("DESC_CLUB_FUND") %></textarea></td>
				<td colspan="2">&nbsp;</td>
			</tr>
			<%=	Bean.getIdCreationAndMoficationRecordFields(
					fund.getValue("ID_CLUB_FUND"),
					fund.getValue(Bean.getCreationDateFieldName()),
					fund.getValue("CREATED_BY"),
					fund.getValue(Bean.getLastUpdateDateFieldName()),
					fund.getValue("LAST_UPDATE_BY")
				) %>
			<tr>
				<td colspan="6" align="center">
					<%=Bean.getSubmitButtonAjax(updateLink,"submit","updateForm","div_detail") %>
					<%=Bean.getResetButton() %>
					<%=Bean.getGoBackButton(backLink) %>
				</td>
			</tr>
		</table>

		</form>
		</div>
		<%= Bean.getCalendarScript("date_beg", false) %>
		<%= Bean.getCalendarScript("date_end", false) %>
	<%
	   	} else {
   	    %> <%= Bean.getUnknownActionText(action) %><%
   		}
	} else if (process.equalsIgnoreCase("yes")) {    

	  	String
	  		id_club_fund_parent 		= Bean.getDecodeParam(parameters.get("id_club_fund_parent")), 
  			name_club_fund 				= Bean.getDecodeParam(parameters.get("name_club_fund")), 
	  		sname_club_fund 			= Bean.getDecodeParam(parameters.get("sname_club_fund")), 
  			desc_club_fund 				= Bean.getDecodeParam(parameters.get("desc_club_fund")), 
  			date_beg 					= Bean.getDecodeParam(parameters.get("date_beg")), 
  			date_end 					= Bean.getDecodeParam(parameters.get("date_end")), 
  			id_club 					= Bean.getDecodeParam(parameters.get("id_club")), 
  			id_jur_prs 					= Bean.getDecodeParam(parameters.get("id_jur_prs")), 
  			LUD		 					= Bean.getDecodeParam(parameters.get("LUD"));
	    
		if (action.equalsIgnoreCase("add")) { 
				
			ArrayList<String> pParam = new ArrayList<String>();
					
			pParam.add(id_club);
			pParam.add(id_club_fund_parent);
			pParam.add(name_club_fund);
			pParam.add(sname_club_fund);
			pParam.add(desc_club_fund);
			pParam.add(date_beg);
			pParam.add(date_end);
			pParam.add(id_jur_prs);
			pParam.add(Bean.getDateFormat());
	
		 	%>
			<%= Bean.executeInsertFunction("PACK$FUND_UI.add_fund", pParam, backLink + "&id_club_fund=", backLink) %>
			<% 	
	
		} else if (action.equalsIgnoreCase("remove")) {
			ArrayList<String> pParam = new ArrayList<String>();
					
			pParam.add(id_club_fund);
			
		 	%>
			<%= Bean.executeDeleteFunction("PACK$FUND_UI.delete_fund", pParam, generalLink , "") %>
			<% 	
	
		} else if (action.equalsIgnoreCase("edit")) {
			ArrayList<String> pParam = new ArrayList<String>();
						
			pParam.add(id_club_fund);
			pParam.add(id_club_fund_parent);
			pParam.add(name_club_fund);
			pParam.add(sname_club_fund);
			pParam.add(desc_club_fund);
			pParam.add(date_beg);
			pParam.add(date_end);
			pParam.add(date_end);
			pParam.add(Bean.getDateFormat());
			System.out.println("backLink="+backLink);
		 	%>
			<%= Bean.executeUpdateFunction("PACK$FUND_UI.update_fund", pParam, backLink, "") %>
			<% 	
	
		} else {%> 
			<%= Bean.getUnknownActionText(action) %><% 
		}
	} else {%> 
		<%= Bean.getUnknownProcessText(process) %><% 
	}
} else  if (type.equalsIgnoreCase("rests")) {
	if (process.equalsIgnoreCase("yes")) {
		if (action.equalsIgnoreCase("run")) {
			String lBackLink = "";
			if (!(Bean.isEmpty(id_club_fund) || "empty".equalsIgnoreCase(id_club_fund))) {
				lBackLink = "../crm/club/fundspecs.jsp?id="+id_club_fund;
			} else {
				lBackLink = "../crm/club/fund.jsp";
			}

			ArrayList<String> pParam = new ArrayList<String>();
							
			pParam.add(id_club_fund);
		  	     
		 	%>
			
			<%= Bean.executeUpdateFunction("PACK$FUND_UI.calc_fund_rests_initial", pParam, lBackLink, "") %>
			<% 	
	    } else { %> 
	    	<%= Bean.getUnknownActionText(action) %><% 
	    }
		  
	} else {%> 
		<%= Bean.getUnknownProcessText(process) %> <%
	}
} else {%> 
	<%= Bean.getUnknownTypeText(type) %> <%
}
  %>

</body>
</html>
