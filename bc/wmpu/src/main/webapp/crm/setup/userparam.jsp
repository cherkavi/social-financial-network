<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcCRMBean"  />

<%= Bean.getLogOutScript(request) %>

<html>
<head>
	<%=Bean.getMetaContent() %>
	<link rel="stylesheet" type="text/css" href="../crm/CSS/forms.css">

	<script language="JavaScript">
		var indx_location;
		function goBack() { history.back();}
		function windowClose() { window.close();}
	</script>
</head>
<body background="">
<% 
   Bean.loginUser.getCurrentUserFeature();
%>
<br><br><br><br>

	<font class="div_title"><%= Bean.userXML.getfieldTransl("t_password", false) %></font>

  <form action="../crm/setup/userparamupdate2.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST">
	<input type="hidden" name="id" value="<%=Bean.loginUser.getValue("ID_USER") %>">
	<input type="hidden" name="action" value="update_password">
	<input type="hidden" name="process" value="yes">
  <table class="tableuserparam">
    <tr>
    	<td><font size=2><%= Bean.userXML.getfieldTransl("password", false) %></font></td> 
		<td><input type="password" name="password" size="16" value="" style="background: white" class="inputfield"></td>
    </tr>
    <tr>
    	<td><font size=2><%= Bean.userXML.getfieldTransl("confirm_password", false) %></font></td> 
		<td><input type="password" name="confirm_password" size="16" value="" style="background: white" class="inputfield"></td>
    </tr>
    <tr>
        <td align="center" colspan="2"> 
			<%=Bean.getSubmitButtonAjax("../crm/setup/userparamupdate2.jsp") %>
        </td>
    </tr>
 </table>
 </form>

<font class="div_title"><%= Bean.userXML.getfieldTransl("t_usersettings", false) %></font>

  	<script>
		var formData = new Array (
			new Array ('valueparam3', 'varchar2', 1),
			new Array ('valueparam2', 'varchar2', 1),
			new Array ('valueparam4', 'varchar2', 1),
			new Array ('valueparam6', 'varchar2', 1)
		);
		function myValidateForm() {
			alert validateForm(formData);
			return validateForm(formData);
		}
	</script>
	
	<form action="../crm/setup/userparamupdate2.jsp" name="updateForm2" id="updateForm2" accept-charset="UTF-8" method="POST">
	<input type="hidden" name="id" value="<%=Bean.loginUser.getValue("ID_USER") %>">
	<input type="hidden" name="action" value="edit">
	<input type="hidden" name="process" value="yes">
	<input type="hidden" name="paramcount" value="6">
 <table class="tableuserparam" >
    <tr>
    	<td><font size=2><%= Bean.userXML.getfieldTransl("NAME_USER", false) %></font></td> <td><input type="text" name="name_user" size="16" value="<%= Bean.loginUser.getValue("NAME_USER") %>" readonly="readonly" style="background: grey" class="inputfield-ro"></td>
    </tr>
    <tr>
    	<td><font size=2><%= Bean.clubXML.getfieldTransl("club", false) %></font></td> 
		<td><input type="hidden" name="nameparam1" value="clubident"><select name="valueparam1" class="inputfield"><%=Bean.getClubListOptions(Bean.getCurrentClubID(), true) %></select></td>
    </tr>
    <tr>
    	<td><font size=2><%= Bean.roleXML.getfieldTransl("name_role", false) %></font></td> 
		<td><input type="hidden" name="nameparam5" value="current_role"><select name="valueparam5" class="inputfield"><%=Bean.getCurrentUserRolesOptions(Bean.loginUser.getParameterValue("CURRENT_ROLE"), true) %></select></td>
    </tr>
    <tr>
    	<td><font size=2><%= Bean.userXML.getfieldTransl("uil", true) %></font></td> 
		<td><input type="hidden" name="nameparam3" value="uil"><select name="valueparam3" class="inputfield"><%=Bean.getLanguageOptions(Bean.loginUser.getParameterValue("UIL"), false) %></select></td>
    </tr>
    <tr>
    	<td><font size=2><%= Bean.userXML.getfieldTransl("dateformat", true) %></font></td> 
		<td><input type="hidden" name="nameparam2" value="dateformat"><select name="valueparam2" class="inputfield"> <%= Bean.getMeaningFromLookupNameOptions("DATE_FORMAT", Bean.loginUser.getParameterValue("DATEFORMAT"), false) %> </select></td>
    </tr>
    <tr>
    	<td><font size=2><%= Bean.userXML.getfieldTransl("report_format", true) %></font></td> 
		<td><input type="hidden" name="nameparam4" value="report_format"><select name="valueparam4" class="inputfield"><%= Bean.getMeaningFromLookupNameOptions("REPORT_FORMAT", Bean.loginUser.getParameterValue("REPORT_FORMAT"), false) %></select></td>
    </tr>
    <tr>
    	<td><font size=2><%= Bean.userXML.getfieldTransl("rows_on_page", true) %></font></td> 
		<td><input type="hidden" name="nameparam6" value="rows_on_page"><select name="valueparam6" class="inputfield"><%= Bean.getMeaningFromLookupNameOrderByNymberValueOptions("ROWS_ON_PAGE", Bean.loginUser.getParameterValue("ROWS_ON_PAGE"), false) %></select></td>
    </tr>
    <tr>
        <td align="center" colspan="4"> 
			<%=Bean.getSubmitButtonAjax("../crm/setup/userparamupdate2.jsp", "submit", "updateForm2") %>
        </td>
    </tr>
 </table>
 </form>

</body>
</html>