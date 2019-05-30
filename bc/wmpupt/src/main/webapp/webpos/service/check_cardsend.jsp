<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>

<%@page import="java.util.HashMap"%>

<%@page import="java.util.ArrayList"%>
<%@page import="java.io.IOException"%>
<%@page import="java.nio.charset.Charset"%>
<%@page import="java.io.PrintWriter"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Date"%>
<%@page import="webpos.wpClubCardObject"%>
<%@page import="bc.util.JndiUtils"%><jsp:useBean id="Bean" scope="session" class="bc.bcWebPosBean"  />

<%
	HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());

	String cd_card1			= Bean.getDecodeParam(parameters.get("cd_card1")); 

	Bean.loginTerm.getFeature();
%>

<% 
	
	wpClubCardObject card = new wpClubCardObject(cd_card1);
	
	SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
	String fileName = "";
	String fileNameFull = "";
	
	try { 
		fileName = "card_" + cd_card1 + "_" + sdf.format(new Date()) + ".txt";
		fileNameFull = JndiUtils.readJndi("${wmpupt/document/uploaded}") + "/" + fileName;
		
		SimpleDateFormat td = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
		PrintWriter writer = new PrintWriter(fileNameFull, "utf-8");
		writer.println("<table>");
		writer.println("<tr><td colspan=\"2\">"+td.format(new Date())+"</td></tr>");
		writer.println(card.getWEBClientCardParamHTML(Bean.loginTerm));
		writer.println("</table>");
		writer.close();
	} catch (IOException e) { // Useful error handling here 
	}
	
	ArrayList<String> pParam = new ArrayList<String>();
	
	pParam.add(cd_card1);
	pParam.add(fileName);
	pParam.add(fileNameFull);
	
	String[] results = new String[2];
	String resultInt 			= "1";
	String resultMessage 		= "";
	
	results 					= Bean.executeFunction("PACK$WEBPOS_UI.send_card_state_by_email", pParam, results.length);
	resultInt 					= results[0];
	resultMessage 				= results[1];
	if (!Bean.C_SUCCESS_RESULT.equalsIgnoreCase(resultInt)) { %>
		<br><span id="error_title"><%=Bean.webposXML.getfieldTransl("operation_error", false) %></span><br>
		<span id="error_description"><%=resultMessage %></span><br><br>
	<%} else { %>
		<br><span style="color:green; font-weight: bold;"><%=resultMessage %></span><br><br>
	<%}
 %>