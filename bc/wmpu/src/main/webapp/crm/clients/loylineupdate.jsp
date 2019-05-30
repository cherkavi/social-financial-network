<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcCRMBean"  />

<%@page import="bc.objects.bcLoySchemeObject"%>
<%@page import="bc.objects.bcLoyLineObject"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.ArrayList"%><html>

<%= Bean.getLogOutScript(request) %>

<head>
	<%= Bean.getMetaContent() %>
	<%= Bean.getBottomFrameCSS() %>

	<script language="JavaScript">
		function createRequest(){
			var request = null;
			try {
				request = new XMLHttpRequest();
			} catch (trymicrosoft) {
				try{
					request = new ActiveXObject("Msxml2.XMLHTTP");			
				} catch (othermicrosoft) {
					try {
						request = new ActiveXObject("Microsoft.XMLHTTP");
					}catch (failed) {
						request = null;
					}
				}
			}
			if (request == null) {
				alert ("Error creating request object")
			} else {
				return request;
			}
		} // end createRequest()
				
		function disable(element_name){
			element = document.getElementById(element_name);
			element.value = '';
			element.className = 'inputfield-ro';
			element.readOnly = 1;
		}
		function enable(element_name){
			element = document.getElementById(element_name);			
			element.className = 'inputfield';
			element.readOnly = 0;
		}
		function setLoy1(){
			enable('OPR_SUM');
			enable('BON_PERCENT_VALUE');
			enable('DISC_PERCENT_VALUE');
			disable('BON_FIXED_VALUE');
			disable('DISC_FIXED_VALUE');
		}
		function setLoy2(){
			disable('OPR_SUM');
			enable('BON_PERCENT_VALUE');
			enable('DISC_PERCENT_VALUE');
			disable('BON_FIXED_VALUE');
			disable('DISC_FIXED_VALUE');
		}
		function setLoy3(){
			enable('OPR_SUM');
			disable('BON_PERCENT_VALUE');
			disable('DISC_PERCENT_VALUE');
			enable('BON_FIXED_VALUE');
			enable('DISC_FIXED_VALUE');
		}
		function checkKindLoy(){
			var loyCode = document.getElementById('CD_KIND_LOYALITY').value;
			if (loyCode == '0001') setLoy1();
			if (loyCode == '0002') setLoy2();
			if (loyCode == '0003') setLoy3();
		}
		
		var formData0001 = new Array (
				new Array ('CD_KIND_LOYALITY', 'varchar2', 1),
				new Array ('ID_CARD_STATUS', 'varchar2', 1),
				new Array ('ID_CATEGORY', 'varchar2', 1),
				new Array ('ACTIVE', 'varchar2', 1),
				new Array ('NULLPERIOD_FLAG', 'varchar2', 1),
				new Array ('OPR_SUM', 'varchar2', 1),
				new Array ('BON_PERCENT_VALUE', 'varchar2', 1),
				new Array ('DISC_PERCENT_VALUE', 'varchar2', 1)
		);
		var formData0002 = new Array (
				new Array ('CD_KIND_LOYALITY', 'varchar2', 1),
				new Array ('ID_CARD_STATUS', 'varchar2', 1),
				new Array ('ID_CATEGORY', 'varchar2', 1),
				new Array ('ACTIVE', 'varchar2', 1),
				new Array ('NULLPERIOD_FLAG', 'varchar2', 1),
				new Array ('BON_PERCENT_VALUE', 'varchar2', 1),
				new Array ('DISC_PERCENT_VALUE', 'varchar2', 1)
		);
		var formData0003 = new Array (
				new Array ('CD_KIND_LOYALITY', 'varchar2', 1),
				new Array ('ID_CARD_STATUS', 'varchar2', 1),
				new Array ('ID_CATEGORY', 'varchar2', 1),
				new Array ('ACTIVE', 'varchar2', 1),
				new Array ('NULLPERIOD_FLAG', 'varchar2', 1),
				new Array ('OPR_SUM', 'varchar2', 1),
				new Array ('BON_FIXED_VALUE', 'varchar2', 1),
				new Array ('DISC_FIXED_VALUE', 'varchar2', 1)
		);
		function myValidateForm(){
			var loyCode = document.getElementById('CD_KIND_LOYALITY').value;
			if (loyCode == '0001') return validateForm(formData0001);
			if (loyCode == '0002') return validateForm(formData0002);
			if (loyCode == '0003') return validateForm(formData0003);
		}
		checkKindLoy();
		dwr_get_card_category(document.getElementById('ID_CARD_STATUS'),'ID_CATEGORY', '<%=Bean.getSessionId() %>');
	</script>


</head>

<body>
<%
HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());

String pageFormName = "CLIENTS_LOY";

Bean.setJspPageForTabName(pageFormName);

request.setCharacterEncoding("UTF-8");

String type					= Bean.getDecodeParamPrepare(parameters.get("type"));
String id_loyality_scheme	= Bean.getDecodeParamPrepare(parameters.get("id_loyality_scheme"));
String id_line				= Bean.getDecodeParamPrepare(parameters.get("id_line"));
String action				= Bean.getDecodeParamPrepare(parameters.get("action")); 
String process				= Bean.getDecodeParamPrepare(parameters.get("process"));

if (type.equalsIgnoreCase("loylines")) { 
	if (process.equalsIgnoreCase("no")) {
		if (action.equalsIgnoreCase("add")) {
    		%>
			<%= Bean.getOperationTitle(
					Bean.loylineXML.getfieldTransl("h_loyality_add", false),
					"Y",
					"N") 
			%>
	        <form action="../crm/clients/loylineupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return myValidateForm()">
				<input type="hidden" name="type" value="loylines">
		        <input type="hidden" name="action" value="add">
	    	    <input type="hidden" name="process" value="yes">
	        	<input type="hidden" name="id_loyality_scheme" value="<%=id_loyality_scheme%>">	        

			<table <%=Bean.getTableDetailParam() %>>
				<tr>
					<td><%=Bean.loylineXML.getfieldTransl("NAME_KIND_LOYALITY", true) %></td><td><select name="CD_KIND_LOYALITY" id="CD_KIND_LOYALITY" class="inputfield" onchange="checkKindLoy(); return false;"><%= Bean.getLoyalityKindOptions("", false) %></select></td>
					<td><%=Bean.loylineXML.getfieldTransl("OPR_SUM", true)  %></td><td><input type="text" name="OPR_SUM" id="OPR_SUM" size="8" value="" class="inputfield"></td>						
				</tr>
				<tr>
					<td><%= Bean.loylineXML.getfieldTransl("NAME_CARD_STATUS", true) %></td><td><select name="ID_CARD_STATUS" id="ID_CARD_STATUS" class="inputfield" onchange="dwr_get_card_category(this,'ID_CATEGORY', '<%=Bean.getSessionId() %>');"><%=Bean.getClubCardStatusOptions("", false) %> </select></td>
					<td><%= Bean.loylineXML.getfieldTransl("BON_PERCENT_VALUE", true) %></td><td><input type="text" name="BON_PERCENT_VALUE" id="BON_PERCENT_VALUE" size="8" value="" class="inputfield"></td>
				</tr>
				<tr>
					<td><%= Bean.loylineXML.getfieldTransl("NAME_CATEGORY", true)  %></td> <td><select name="ID_CATEGORY" id="ID_CATEGORY" class="inputfield"><%=Bean.getClubCardCategoryOptions("", false)%></select></td>
					<td><%= Bean.loylineXML.getfieldTransl("BON_FIXED_VALUE", true) %></td> <td><input type="text" name="BON_FIXED_VALUE" id="BON_FIXED_VALUE" size="8" value="" class="inputfield"></td>
				</tr>
				<tr>
					<td><%= Bean.loylineXML.getfieldTransl("ACTIVE_TSL", true)  %></td> <td><select name="ACTIVE" id="ACTIVE" class="inputfield"><%= Bean.getMeaningFromLookupNumHTML("YES_NO", "1", false) %></select></td>
					<td><%= Bean.loylineXML.getfieldTransl("DISC_PERCENT_VALUE", true) %></td> <td><input type="text" name="DISC_PERCENT_VALUE" id="DISC_PERCENT_VALUE" size="8" value="" class="inputfield"> </td>
				<tr>
					<td><%= Bean.loylineXML.getfieldTransl("NULLPERIOD_FLAG", true)  %></td> <td><select name="NULLPERIOD_FLAG" id="NULLPERIOD_FLAG" class="inputfield"><%= Bean.getMeaningFromLookupNumHTML("YES_NO", "1", false) %></select></td>
					<td><%= Bean.loylineXML.getfieldTransl("DISC_FIXED_VALUE", true)  %></td> <td><input type="text" name="DISC_FIXED_VALUE" id="DISC_FIXED_VALUE" size="8" value="" class="inputfield"></td>
				</tr>
				<tr>
					<td colspan="6" align="center">
						<%=Bean.getSubmitButtonAjax("../crm/clients/loylineupdate.jsp") %>
						<%=Bean.getResetButton() %>
						<%=Bean.getGoBackButton("../crm/clients/loyspecs.jsp?id="+id_loyality_scheme) %>
					</td>
				</tr>
			</table>		
		</form> <%			
		} else if (action.equalsIgnoreCase("edit")) {
			bcLoyLineObject line = new bcLoyLineObject(id_line);
    		%>
			<%= Bean.getOperationTitle(
					Bean.loylineXML.getfieldTransl("h_loyality_edit", false),
					"Y",
					"N") 
			%>
	        <form action="../crm/clients/loylineupdate.jsp" name="updateForm" id="updateForm" accept-charset="UTF-8" method="POST" onSubmit="return myValidateForm()" >
				<input type="hidden" name="type" value="loylines">
		        <input type="hidden" name="action" value="edit">
	    	    <input type="hidden" name="process" value="yes">
	        	<input type="hidden" name="id_loyality_scheme" value="<%=id_loyality_scheme%>">
	        	<input type="hidden" name="id_line" value="<%=id_line%>">	        

			<table <%=Bean.getTableDetailParam() %>>
				<tr>
					<td><%=Bean.loylineXML.getfieldTransl("NAME_KIND_LOYALITY", true) %></td><td><select name="CD_KIND_LOYALITY" id="CD_KIND_LOYALITY" class="inputfield" onchange="checkKindLoy(); return false;"><%= Bean.getLoyalityKindOptions(line.getValue("CD_KIND_LOYALITY"), false) %></select></td>
					<td><%=Bean.loylineXML.getfieldTransl("OPR_SUM", true)  %></td><td><input type="text" name="OPR_SUM" id="OPR_SUM" size="8" value="<%=line.getValue("OPR_SUM_FRMT") %>" class="inputfield"></td>						
				</tr>
				<tr>
					<td><%= Bean.loylineXML.getfieldTransl("NAME_CARD_STATUS", true) %></td><td><select name="ID_CARD_STATUS" id="ID_CARD_STATUS" class="inputfield" onchange="dwr_get_card_category(this,'ID_CATEGORY', '<%=Bean.getSessionId() %>');"><%=Bean.getClubCardStatusOptions(line.getValue("ID_CARD_STATUS"), false) %> </select></td>
					<td><%= Bean.loylineXML.getfieldTransl("BON_PERCENT_VALUE", true) %></td><td><input type="text" name="BON_PERCENT_VALUE" id="BON_PERCENT_VALUE" size="8" value="<%=line.getValue("BON_PERCENT_VALUE_FRMT") %>" class="inputfield"></td>
				</tr>
				<tr>
					<td><%= Bean.loylineXML.getfieldTransl("NAME_CATEGORY", true)  %></td> <td><select name="ID_CATEGORY" id="ID_CATEGORY" class="inputfield"><OPTION value=<%=line.getValue("ID_CATEGORY")%>><%=line.getValue("NAME_CATEGORY") %></OPTION></select></td>
					<td><%= Bean.loylineXML.getfieldTransl("BON_FIXED_VALUE", true) %></td> <td><input type="text" name="BON_FIXED_VALUE" id="BON_FIXED_VALUE" size="8" value="<%=line.getValue("BON_FIXED_VALUE_FRMT") %>" class="inputfield"></td>
				</tr>
				<tr>
					<td><%= Bean.loylineXML.getfieldTransl("ACTIVE_TSL", true)  %></td> <td><select name="ACTIVE" name="ACTIVE" class="inputfield"><%= Bean.getMeaningFromLookupNumHTML("YES_NO", line.getValue("ACTIVE"), true) %></select></td>
					<td><%= Bean.loylineXML.getfieldTransl("DISC_PERCENT_VALUE", true) %></td> <td><input type="text" name="DISC_PERCENT_VALUE" id="DISC_PERCENT_VALUE" size="8" value="<%=line.getValue("DISC_PERCENT_VALUE_FRMT") %>" class="inputfield"> </td>
				<tr>
					<td><%= Bean.loylineXML.getfieldTransl("NULLPERIOD_FLAG", true)  %></td> <td><select name="NULLPERIOD_FLAG" id="NULLPERIOD_FLAG" class="inputfield"><%= Bean.getMeaningFromLookupNumHTML("YES_NO", line.getValue("NULLPERIOD_FLAG"), true) %></select></td>
					<td><%= Bean.loylineXML.getfieldTransl("DISC_FIXED_VALUE", true)  %></td> <td><input type="text" name="DISC_FIXED_VALUE" id="DISC_FIXED_VALUE" size="8" value="<%=line.getValue("DISC_FIXED_VALUE_FRMT") %>" class="inputfield"></td>
				</tr>
				<%=	Bean.getIdCreationAndMoficationRecordFields(
						line.getValue("ID_LINE"),
						line.getValue(Bean.getCreationDateFieldName()),
						line.getValue("CREATED_BY"),
						line.getValue(Bean.getLastUpdateDateFieldName()),
						line.getValue("LAST_UPDATE_BY")
					) %>
				<tr>
					<td colspan="6" align="center">
						<%=Bean.getSubmitButtonAjax("../crm/clients/loylineupdate.jsp") %>
						<%=Bean.getResetButton() %>
						<%=Bean.getGoBackButton("../crm/clients/loyspecs.jsp?id="+id_loyality_scheme) %>
					</td>
				</tr>
			</table>		
		</form> <%
		}
    	else {
    	    %> <%= Bean.getUnknownActionText(action) %><%
    	}
	} else if (process.equalsIgnoreCase("yes")) {
		
		String
			CD_KIND_LOYALITY 	= Bean.getDecodeParam(parameters.get("CD_KIND_LOYALITY")), 
	    	OPR_SUM 			= Bean.getDecodeParam(parameters.get("OPR_SUM")), 
	    	ID_CARD_STATUS 		= Bean.getDecodeParam(parameters.get("ID_CARD_STATUS")), 
	    	BON_PERCENT_VALUE 	= Bean.getDecodeParam(parameters.get("BON_PERCENT_VALUE")), 
	    	ID_CATEGORY 		= Bean.getDecodeParam(parameters.get("ID_CATEGORY")),
	    	BON_FIXED_VALUE 	= Bean.getDecodeParam(parameters.get("BON_FIXED_VALUE")), 
	    	ACTIVE 				= Bean.getDecodeParam(parameters.get("ACTIVE")), 
	    	DISC_PERCENT_VALUE 	= Bean.getDecodeParam(parameters.get("DISC_PERCENT_VALUE")), 
	    	DISC_FIXED_VALUE 	= Bean.getDecodeParam(parameters.get("DISC_FIXED_VALUE")), 
	    	NULLPERIOD_FLAG 	= Bean.getDecodeParam(parameters.get("NULLPERIOD_FLAG"));
	    
	    String callSQL = "";
	    
	    ArrayList<String> pParam = new ArrayList<String>();
    
		if (action.equalsIgnoreCase("add")) {
			pParam.add(id_loyality_scheme);
			pParam.add(CD_KIND_LOYALITY);
			pParam.add(ID_CARD_STATUS);
			pParam.add(ID_CATEGORY);
			pParam.add(OPR_SUM);
			pParam.add(BON_PERCENT_VALUE);
			pParam.add(BON_FIXED_VALUE);
			pParam.add(DISC_PERCENT_VALUE);
			pParam.add(DISC_FIXED_VALUE);
			pParam.add(NULLPERIOD_FLAG);
			pParam.add(ACTIVE);
	
		 	%>
			<%= Bean.executeInsertFunction("PACK$LOY_UI.add_loyality_line", pParam, "../crm/clients/loyspecs.jsp?id=" + id_loyality_scheme + "&id_line=", "") %>
			<% 	

		} else if (action.equalsIgnoreCase("edit")) { 
			
			pParam.add(id_line);
			pParam.add(id_loyality_scheme);
			pParam.add(CD_KIND_LOYALITY);
			pParam.add(ID_CARD_STATUS);
			pParam.add(ID_CATEGORY);
			pParam.add(OPR_SUM);
			pParam.add(BON_PERCENT_VALUE);
			pParam.add(BON_FIXED_VALUE);
			pParam.add(DISC_PERCENT_VALUE);
			pParam.add(DISC_FIXED_VALUE);
			pParam.add(NULLPERIOD_FLAG);
			pParam.add(ACTIVE);

		 	%>
			<%= Bean.executeUpdateFunction("PACK$LOY_UI.update_loyality_line", pParam, "../crm/clients/loyspecs.jsp?id=" + id_loyality_scheme, "") %>
			<% 	

		} else if (action.equalsIgnoreCase("remove")) { 
		
			pParam.add(id_line);

		 	%>
			<%= Bean.executeDeleteFunction("PACK$LOY_UI.delete_loyality_line", pParam, "../crm/clients/loyspecs.jsp?id=" + id_loyality_scheme, "") %>
			<% 	
		
		} else {
    	    %> <%= Bean.getUnknownActionText(action) %><%
    	}
	} else {
	    %> <%= Bean.getUnknownProcessText(process) %> <%
	}
} else {
    %><%= Bean.getUnknownTypeText(type) %><%
}
  %>

</body>
</html>
