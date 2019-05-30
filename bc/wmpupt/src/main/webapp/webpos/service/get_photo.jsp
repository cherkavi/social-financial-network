<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcWebPosBean"  />

<%@page import="java.util.HashMap"%>
<%@page import="java.util.ArrayList"%>
<%@page import="webpos.wpNatPrsObject"%>
<%@page import="webpos.wpNatPrsRoleObject"%>
<%@page import="org.apache.commons.fileupload.servlet.ServletFileUpload"%>
<%@page import="org.apache.commons.fileupload.FileItemFactory"%>
<%@page import="org.apache.commons.fileupload.disk.DiskFileItemFactory"%>
<%@page import="java.util.List"%>
<%@page import="org.apache.commons.fileupload.FileItem"%>
<%@page import="java.io.File"%>
<%@page import="java.awt.Image"%>
<%@page import="javax.imageio.ImageIO"%><html>
<head>
	<%= Bean.getMetaContent() %>

</head>
<body>
<%
request.setCharacterEncoding("UTF-8");

HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());

String pageFormName = "WEBPOS_SERVICE_CARD_ACTIVATION";

Bean.setJspPageForTabName(pageFormName);

Bean.readWebPosMenuHTML();

request.setCharacterEncoding("UTF-8");
String action		= Bean.getDecodeParam(parameters.get("action")); 

String id_role		= Bean.getDecodeParam(parameters.get("id_role"));
String id_term	 	= Bean.getCurrentTerm();

action 	= Bean.isEmpty(action)?"":action;

	if (action.equalsIgnoreCase("update")) {
	 	//wpOnlineOperationObject oper = new wpOnlineOperationObject(id_telgr);
				
		wpNatPrsRoleObject role = new wpNatPrsRoleObject(id_role);%>

		<img src="/NatPrsPhoto?id_role=<%=id_role %>" style="border:0">
		
		<%
	} else { 
		%> 
		<%= Bean.getUnknownActionText(action) %><%
	}
%>


</body>
</html>
