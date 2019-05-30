<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>

<%@page import="webpos.wpChequeObject"%>
<%@page import="java.util.HashMap"%>

<%@page import="java.util.ArrayList"%>
<%@page import="java.io.IOException"%>
<%@page import="java.nio.charset.Charset"%>
<%@page import="java.io.PrintWriter"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="bc.util.JndiUtils"%>
<%@page import="java.util.Date"%>

<jsp:useBean id="Bean" scope="session" class="bc.bcWebPosBean"  />

<%
	HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());

	String send_email		= Bean.getDecodeParam(parameters.get("send_EMAIL")); 
	String send_sms			= Bean.getDecodeParam(parameters.get("send_SMS")); 
	String id_telgr			= Bean.getDecodeParam(parameters.get("id_telgr")); 

	Bean.loginTerm.getTermFeature();
%>

<% if ("Y".equalsIgnoreCase(send_email) || "Y".equalsIgnoreCase(send_sms)) {
	
	wpChequeObject cheque = new wpChequeObject(id_telgr, "TXT", Bean.loginTerm);
	
	String fileName = "";
	String fileNameFull = "";
	
	if ("Y".equalsIgnoreCase(send_email)) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		fileName = "cheque_" + id_telgr + "_" + sdf.format(new Date()) + ".txt";
		fileNameFull = JndiUtils.readJndi("${wmpupt/document/uploaded}") + "/" + fileName;
		
		try { 
			PrintWriter writer = new PrintWriter(fileNameFull, "utf-8");
			writer.println(cheque.getChequeSaveData(true));
			writer.close();
		} catch (IOException e) { 
			System.out.println("EXCEPTION: "+ e.toString());
		}
	}
	
	ArrayList<String> pParam = new ArrayList<String>();
	
	pParam.add(id_telgr);
	pParam.add(send_email);
	pParam.add(fileName);
	pParam.add(fileNameFull);
	pParam.add(send_sms);
	
	String[] results = new String[2];
	String resultInt 			= "1";
	String resultMessage 		= "";
	
	results 					= Bean.executeFunction("PACK$WEBPOS_UI.send_cheque", pParam, results.length);
	resultInt 					= results[0];
	resultMessage 				= results[1];
	if (!Bean.C_SUCCESS_RESULT.equalsIgnoreCase(resultInt)) { %>
		<br><span id="error_title"><%=Bean.webposXML.getfieldTransl("operation_error", false) %></span><br>
		<span id="error_description"><%=resultMessage %></span><br><br>
	<%} else { %>
		<br><span style="color:green; font-weight: bold;"><%=resultMessage %></span><br><br>
	<%}
} %>
