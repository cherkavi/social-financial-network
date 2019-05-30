<%@ page import="java.util.HashMap, bc.connection.Connector, java.sql.Connection, bc.AppConst, java.text.SimpleDateFormat"%>
<%@ page import="java.util.List" %>
<%@ page import="bc.payment.*" %>
<%@ page import="bc.util.DateUtils" %>
<%@ page import="org.apache.log4j.Logger" %>
<%@ page 
	language="java" 
	contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@page import="bc.payment.robokassa.RobokassaPaymentService"%>
<%@page import="org.apache.commons.collections.MapUtils"%>
<%@page import="bc.payment.robokassa.RobokassaUtils"%><html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<meta http-equiv="cache-control" content="max-age=0" />
		<meta http-equiv="cache-control" content="no-cache" />
		<meta http-equiv="expires" content="0" />
		<meta http-equiv="expires" content="Tue, 01 Jan 1980 1:00:00 GMT" />
		<meta http-equiv="pragma" content="no-cache" />
		<title>Payment confirmation</title>
	</head>
	<body>
<%
// this page will be requested by external service
Logger LOGGER = Logger.getLogger( "/payments/robokassa/paymentResult" );
String sessionId=request.getSession().getId();
RobokassaPaymentService service=new RobokassaPaymentService();
Connection connection=null;
try{
	// read parameters
	String parameterPaymentId=request.getParameter(RobokassaUtils.ROBOPARAM_INV_ID);
	String parameterPaymentAmount=request.getParameter(RobokassaUtils.ROBOPARAM_OUT_SUM);
	String parameterPaymentCrc=request.getParameter(RobokassaUtils.ROBOPARAM_SIGN_VALUE);

	// get connection 
	connection=Connector.getAdminConnection(sessionId, "RU");
	if(connection==null){
		// TODO ??? - do we need to return 501..505 ? ( internal server error )
		response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		return;
	}
	
	// check response 
	// ??? maybe need to filter by external address: 
	// System.out.println("Robokassa RemoteAddr: "+request.getRemoteAddr()); // 212.24.63.65
	// System.out.println("Robokassa RemoteHost: "+request.getRemoteHost());
	// check response - check CRC - need by protocol
	if(!RobokassaUtils.isCrcResultFromRobokassaValid(parameterPaymentId, parameterPaymentAmount, parameterPaymentCrc)){
		// TODO !!! ALERT
		response.sendError(HttpServletResponse.SC_BAD_REQUEST); // SC_EXPECTATION_FAILED
		return;
	}
	
	if(service.isPaymentSuccessful(Long.parseLong(parameterPaymentId))){
		LOGGER.info("payment.Id: "+parameterPaymentId+" ACKNOWLEDGED ");
		service.setPaymentExternalSystemResult(connection, parameterPaymentId, PaymentResultOfExternalService.ACKNOWLEDGED);
	}else{
		// TODO !!! ALERT Place for catching of the FRAUD
		LOGGER.info("payment.Id: "+parameterPaymentId+" still not payed");
	}
	
}finally{
	Connector.closeConnection(connection);
	Connector.removeSessionId(sessionId);
}
%>
	</body>
</html>