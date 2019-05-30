<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcCRMBean"  />

<%= Bean.getLogOutScript(request) %>

<%@page import="bc.objects.bcCallCenterFAQObject"%>
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

String pageFormName = "CALL_CENTER_FAQ";

Bean.setJspPageForTabName(pageFormName);

request.setCharacterEncoding("UTF-8");

String type		= Bean.getDecodeParam(parameters.get("type"));
String id		= Bean.getDecodeParam(parameters.get("id"));
String action	= Bean.getDecodeParam(parameters.get("action")); 
String process	= Bean.getDecodeParam(parameters.get("process"));

if (id==null || ("".equalsIgnoreCase(id))) id="empty";
if (action==null || ("".equalsIgnoreCase(action))) action="empty";
if (process==null || ("".equalsIgnoreCase(process))) process="empty";
if (type==null || ("".equalsIgnoreCase(type))) type="empty";

if (type.equalsIgnoreCase("general")) {
	if (process.equalsIgnoreCase("no"))
	/* вибираємо тип дії (добавити, видалити...)*/
	{
	   /*  --- Добавити запис --- */
    	if (action.equalsIgnoreCase("add")|| action.equalsIgnoreCase("add2")) {
    	
    		bcClubShortObject club = new bcClubShortObject(Bean.getCurrentClubID());
    	%>
	<script>
		var formDataFAQ = new Array (
			new Array ('cd_cc_faq', 'varchar2', 1),
			new Array ('id_cc_faq_category', 'varchar2', 1),
			new Array ('title_cc_faq', 'varchar2', 1),
			new Array ('question_cc_faq', 'varchar2', 1),
			new Array ('answer_cc_faq', 'varchar2', 1),
			new Array ('name_club', 'varchar2', 1),
			new Array ('exist_flag', 'varchar2', 1)
		);
		function myValidateForm() {
			return validateForm(formDataFAQ);
		}
	</script> 
		<%= Bean.getOperationTitle(
				Bean.call_centerXML.getfieldTransl("h_add_faq", false),
				"Y",
				"Y") 
		%>
    <form action="../crm/call_center/faqupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return myValidateForm()">
	        <input type="hidden" name="type" value="general">
	        <input type="hidden" name="action" value="add">
	        <input type="hidden" name="process" value="yes">
	<table <%=Bean.getTableDetailParam() %>>
		
		<tr>
			<td><%= Bean.call_centerXML.getfieldTransl("cd_cc_faq", true) %></td><td><input type="text" name="cd_cc_faq" size="20" value="" class="inputfield"></td>
			<td><%= Bean.clubXML.getfieldTransl("club", true) %>
				<%= Bean.getGoToClubLink(club.getValue("ID_CLUB")) %>
		  	</td>
		  	<td>
				<%=Bean.getWindowFindClub("club", club.getValue("ID_CLUB"), club.getValue("SNAME_CLUB"), "25") %>
		  	</td>
		</tr>
		<tr>
			<td><%= Bean.call_centerXML.getfieldTransl("id_cc_faq_category", true) %></td><td><select name="id_cc_faq_category" class="inputfield"><%= Bean.getCallCenterFAQCategoryOptions("", false) %></select></td>
			<td><%= Bean.call_centerXML.getfieldTransl("exist_flag", true) %></td><td><select name="exist_flag" class="inputfield"><%= Bean.getYesNoLookupOptions("Y", false) %></select></td>
		</tr>
		<tr>
			<td><%= Bean.call_centerXML.getfieldTransl("title_cc_faq", true) %></td><td  colspan="3"><input type="text" name="title_cc_faq" size="125" value="" class="inputfield"></td>
		</tr>
		<tr>
			<td><%= Bean.call_centerXML.getfieldTransl("question_cc_faq", true) %></td><td  colspan="3"><textarea name="question_cc_faq" cols="120" rows="2" class="inputfield"></textarea></td>
		</tr>
		<tr>
			<td><%= Bean.call_centerXML.getfieldTransl("answer_cc_faq", true) %></td><td  colspan="3"><textarea name="answer_cc_faq" cols="120" rows="6" class="inputfield"></textarea></td>
		</tr>
 		<tr>
			<td colspan="4" align="center">
				<%=Bean.getSubmitButtonAjax("../crm/call_center/faqupdate.jsp") %>
				<%=Bean.getResetButton() %>
				<% if (action.equalsIgnoreCase("add")) { %>
					<%=Bean.getGoBackButton("../crm/call_center/faq.jsp") %>
				<% } else { %>
					<%=Bean.getGoBackButton("../crm/call_center/faqspecs.jsp?id=" + id) %>
				<% } %>
			</td>
		</tr>

	</table>
	

</form>

	        <%
    	} else {
    	    %> <%= Bean.getUnknownActionText(action) %><%
    	}
	}

	else if (process.equalsIgnoreCase("yes")){
    	String
    		cd_cc_faq 				= Bean.getDecodeParam(parameters.get("cd_cc_faq")),
    		question_cc_faq 		= Bean.getDecodeParam(parameters.get("question_cc_faq")),
    		answer_cc_faq 			= Bean.getDecodeParam(parameters.get("answer_cc_faq")),
    		id_cc_faq_category 		= Bean.getDecodeParam(parameters.get("id_cc_faq_category")),
    		exist_flag 				= Bean.getDecodeParam(parameters.get("exist_flag")),
    		title_cc_faq 			= Bean.getDecodeParam(parameters.get("title_cc_faq")),
    		id_club		 			= Bean.getDecodeParam(parameters.get("id_club"));


		if (action.equalsIgnoreCase("add")) { 

			String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_CALL_CENTER.add_faq(" +
				"?,?,?,?,?,?,?,?,?)}";

			ArrayList<String> pParam = new ArrayList<String>();
					
			pParam.add(cd_cc_faq);
			pParam.add(title_cc_faq);
			pParam.add(question_cc_faq);
			pParam.add(answer_cc_faq);
			pParam.add(id_cc_faq_category);
			pParam.add(exist_flag);
			pParam.add(id_club);
		
			%>
			<%= Bean.getCallResultParam("INSERT", callSQL, pParam, "../crm/call_center/faqspecs.jsp?id=", "../crm/call_center/faq.jsp") %>
			<% 	

		} else if (action.equalsIgnoreCase("remove")) { 

		   	String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_CALL_CENTER.delete_faq(?,?)}";

		   	ArrayList<String> pParam = new ArrayList<String>();
						
			pParam.add(id);

			%>
			<%= Bean.getCallResultParam("DELETE", callSQL, pParam, "../crm/call_center/faq.jsp", "") %>
			<%
		
		} else if (action.equalsIgnoreCase("edit")) { 
		
	 		String callSQL = "{? = call " + Bean.getGeneralDBScheme() + ".PACK_UI_CALL_CENTER.update_faq(" +
	 			"?,?,?,?,?,?,?,?)}";

	 		ArrayList<String> pParam = new ArrayList<String>();
						
			pParam.add(id);
			pParam.add(cd_cc_faq);
			pParam.add(title_cc_faq);
			pParam.add(question_cc_faq);
			pParam.add(answer_cc_faq);
			pParam.add(id_cc_faq_category);
			pParam.add(exist_flag);
		
			%>
			<%= Bean.getCallResultParam("UPDATE", callSQL, pParam, "../crm/call_center/faqspecs.jsp?id=" + id, "") %>
			<% 	
		} else { %> 
			<%= Bean.getUnknownActionText(action) %><% 
		}
	} else {%> 
		<%= Bean.getUnknownProcessText(process) %> <%
	}
} else {
    %> <%= Bean.getUnknownTypeText(type) %> <%
}
%>


</body>
</html>
