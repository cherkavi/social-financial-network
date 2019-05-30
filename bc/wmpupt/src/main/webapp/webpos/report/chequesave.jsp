<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>

<%@page import="webpos.wpChequeObject"%>
<%@page import="java.util.HashMap"%>	


<jsp:useBean id="Bean" scope="session" class="bc.bcWebPosBean"  />

<%
	HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());

	String id_telgr		= Bean.getDecodeParam(parameters.get("id_telgr")); 

	Bean.loginTerm.getTermFeature();
	
	String cheque_format		= Bean.getDecodeParam(parameters.get("cheque_format"));
	
	cheque_format = Bean.isEmpty(cheque_format)?Bean.getChequeSaveFormat():cheque_format;
	
	wpChequeObject cheque = new wpChequeObject(id_telgr, cheque_format, Bean.loginTerm);
	
	String filename = cheque.getFileName(Bean.getChequeSaveFormat());

	response.setContentType("APPLICATION/OCTET-STREAM");   
	response.setHeader("Content-Disposition","attachment; filename=\"" + filename + "\"");   

	out.write(cheque.getChequeSaveData(true));
	out.flush();
%>
