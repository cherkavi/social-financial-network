<%@ page import="java.util.HashMap, bc.connection.Connector, java.sql.Connection, bc.AppConst, java.text.SimpleDateFormat"%>
<%@ page import="java.util.List" %>
<%@ page import="bc.payment.*" %>
<%@ page import="bc.payment.robokassa.*" %>
<%@ page import="bc.payment.exception.*" %>
<%@ page import="bc.util.DateUtils" %>
<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="bc.payment.robokassa.RobokassaUrl"%>
<%@ page import="bc.payment.robokassa.RobokassaUtils"%>
<%@ page import="bc.payment.robokassa.RobokassaFinder"%>
<%@ page import="bc.payment.robokassa.RobokassaPaymentService"%>
<%@ page 
	language="java" 
	contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
Logger LOGGER = Logger.getLogger( "/payments/robokassa/failResult" );
String sessionId=request.getSession().getId();
Connection connection=null;
try{
	// retrieve connection from Database
	connection=Connector.getAdminConnection("RU");
	if(connection==null){
		response.sendRedirect(request.getContextPath()+AppConst.WMPU_MAIN_PAGE);
	}
%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<meta http-equiv="cache-control" content="max-age=0" />
		<meta http-equiv="cache-control" content="no-cache" />
		<meta http-equiv="expires" content="0" />
		<meta http-equiv="expires" content="Tue, 01 Jan 1980 1:00:00 GMT" />
		<meta http-equiv="pragma" content="no-cache" />
				
		<title>list of payments</title>
	</head>
	<body>
		<%
			String outSum=request.getParameter(RobokassaUtils.ROBOPARAM_OUT_SUM);
			String invId=request.getParameter(RobokassaUtils.ROBOPARAM_INV_ID);
			// TODO need to change View
		%>
		<h3> still not payed </h3>		
	</body>
<% 
}finally{
	Connector.closeConnection(connection);
	Connector.removeSessionId(sessionId);
}
%>
</html>
