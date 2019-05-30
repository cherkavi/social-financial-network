<%@ page import="java.util.HashMap, bc.connection.Connector, java.sql.Connection, bc.AppConst, java.text.SimpleDateFormat"%>
<%@ page import="java.util.List" %>
<%@ page import="bc.payment.*" %>
<%@ page import="bc.payment.robokassa.*" %>
<%@ page import="bc.payment.exception.*" %>
<%@ page import="bc.util.DateUtils" %>
<%@ page 
	language="java" 
	contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="bc.payment.robokassa.RobokassaUrl"%>
<% 
// retrieve connection from Database
Connection connection=null;
try{
	connection=Connector.getConnection(request.getSession().getId());
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
	  	<!-- calendar stylesheet -->
	  	<link rel="stylesheet" type="text/css" media="all" href="<%=request.getContextPath()%>/js/jscalendar-1.0/calendar-win2k-cold-1.css" title="win2k-cold-1" />
	  	<!-- main calendar program -->
	  	<script type="text/javascript" src="<%=request.getContextPath()%>/js/jscalendar-1.0/calendar.js"></script>
	  	<!-- language for the calendar -->
	  	<script type="text/javascript" src="<%=request.getContextPath()%>/js/jscalendar-1.0/lang/calendar-en.js"></script>
	  	<!-- the following script defines the Calendar.setup helper function, which makes adding a calendar a matter of 1 or 2 lines of code. -->
	  	<script type="text/javascript" src="<%=request.getContextPath()%>/js/jscalendar-1.0/calendar-setup.js"></script>
	</head>
	<body>
		<hr />
		<form>
			<div align="center">
				<span  style="border-style: solid; border-width: 1px; padding: 5px; ">
					<span>begin:</span>
					<input 
						id="date_begin_text"
						name="date_begin_text"
						size="10" 
						type="text"
						value="<%=( request.getParameter("date_begin_text")==null?"":request.getParameter("date_begin_text") )%>" 
						readonly="readonly" />
					<img 
						id="date_begin_button" 
						src="<%=request.getContextPath()%>/js/jscalendar-1.0/img.gif" 
						style="cursor: pointer; border: 1px solid red;"
						title="Date selector" 
						onmouseover="this.style.background='red';" 
						onmouseout="this.style.background=''" />
					<script type="text/javascript">
					    Calendar.setup({
					        inputField     :    "date_begin_text",     // id of the input field
					        ifFormat       :    "%d.%m.%Y",      // format of the input field
					        button         :    "date_begin_button",  // trigger for the calendar (button ID)
					        align          :    "Tl",           // alignment (defaults to "Bl")
					        singleClick    :    true
					    });
					</script>
				</span>
				&nbsp;
				<span  style="border-style: solid; border-width: 1px; padding: 5px; ">
					<span>end:</span>
					<input 
						id="date_end_text"
						name="date_end_text"
						size="10" 
						type="text"
						value="<%=( request.getParameter("date_end_text")==null?"":request.getParameter("date_end_text")  )%>" 
						readonly="readonly" />
					<img 
						id="date_end_button" 
						src="<%=request.getContextPath()%>/js/jscalendar-1.0/img.gif" 
						style="cursor: pointer; border: 1px solid red;"
						title="Date selector" 
						onmouseover="this.style.background='red';" 
						onmouseout="this.style.background=''" />
					<script type="text/javascript">
					    Calendar.setup({
					        inputField     :    "date_end_text",     // id of the input field
					        ifFormat       :    "%d.%m.%Y",      // format of the input field
					        button         :    "date_end_button",  // trigger for the calendar (button ID)
					        align          :    "Tl",           // alignment (defaults to "Bl")
					        singleClick    :    true
					    });
					</script>
				</span>
			</div>
			<div align="center">
				<input type="submit" value="submit" style="cursor: pointer; margin:5px" />
			</div>
			
		</form>
		
		<hr />
		<div>
			<div align="center">
				result panel
			</div>
			<%
			// robokassa elements 
			ExternalSystemUrl urlGenerator=new RobokassaUrl();
			PaymentExternalFinder finder=new RobokassaFinder();
			
			List<PaymentExternalVO> payments=finder.find(connection, DateUtils.parseDate(request.getParameter("date_begin_text")), DateUtils.parseDate(request.getParameter("date_end_text")), PaymentType.WAITING);
			if(payments==null || payments.size()==0){%>
			<div>
			<b>=== empty ===</b>
			</div>  				
			<%}else{%>
			<div>
				<table border="1">
					<thead>
						<tr>
							<th>Number</th>
							<th>Description</th>
							<th align="center">Amount</th>
							<th align="center">Action</th>
						</tr>
					</thead>
					<tbody>
						<% for( PaymentExternalVO eachPayment:payments ) { %>
						<tr>
							<td align="right"><%=eachPayment.getId()%></td>
							<td><%=eachPayment.getDescription()%></td>
							<td><%=eachPayment.getAmount()%></td>
							<td align="right">
								<% switch(eachPayment.getType()){
									// TODO create page with information about payment
									case PAYED: {%> <a target="_blank" href=""> information </a> <%}; break;
									case CANCELLED: {%><%}; break;
									case WAITING: {%> <a target="_blank" href="<%=urlGenerator.makeExternalLink(eachPayment)%>"> GO TO PAY</a> <%}; break;
								}
								%> 
							</td>
						</tr>
						<% } %>		
					</tbody>
				</table>
				<%=request.getParameter("date_begin_text")%>
				<br />
				<%=request.getParameter("date_end_text")%>
			</div>
			<%}%>
		</div>
	</body>
<% 
}finally{
	Connector.closeConnection(connection);	
}
%>
</html>
