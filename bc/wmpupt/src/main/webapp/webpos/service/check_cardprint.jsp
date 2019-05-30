<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="Bean" scope="session" class="bc.bcWebPosBean"  />

<%@page import="java.util.HashMap"%>

<%@page import="java.util.ArrayList"%>
<%@page import="java.io.IOException"%>
<%@page import="java.nio.charset.Charset"%>
<%@page import="java.io.PrintWriter"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Date"%>
<%@page import="webpos.wpClubCardObject"%>
<html>
	<link rel="stylesheet" type="text/css" href="../<%=Bean.getThemePath()%>/CSS/webpos.css">
	<script type="text/javascript" src="../js/frame_emulator.js" > </script>
	<script type="text/javascript" src="../js/webpos.js" > </script>
	<title>WebPOS - <%=Bean.webposXML.getfieldTransl("window_cheque", false) %></title>

	<meta content="width=device-width, initial-scale=0.8,maximum-scale=1.5,user-scalable=1" name="viewport">
	
	<link content="yes" name="apple-mobile-web-app-capable">
	<meta content="black" name="apple-mobile-web-app-status-bar-style">
<head>
<%
	HashMap<String,String> parameters=Bean.getUtfParameters(request.getQueryString());

	String cd_card1			= Bean.getDecodeParam(parameters.get("cd_card1")); 
	String print		= Bean.getDecodeParam(parameters.get("print")); 

	Bean.loginTerm.getFeature();
%>

	<script language="javascript">
	function printDiv(divName) {
	    var printContents = document.getElementById(divName).innerHTML;
	    var originalContents = document.body.innerHTML;
	    document.body.innerHTML = printContents;
	    window.print();
	    document.body.innerHTML = originalContents;
	}
	function printDivShort(divName) {
	    window.print();
	}

    var gAutoPrint = true;
    function processPrint(divName){
    	if (document.getElementById != null){
    		var html = '<HTML>\n<HEAD>\n';
    		if (document.getElementsByTagName != null){
    			var headTags = document.getElementsByTagName("head");
    			if (headTags.length > 0) html += headTags[0].innerHTML;
    		}
    		html += '\n</HE' + 'AD>\n<BODY>\n';
    		var printReadyElem = document.getElementById(divName);
    		if (printReadyElem != null) html += printReadyElem.innerHTML;
    		else{
    			alert("Error, no contents.");
    			return;
    		}

    		html += '\n</BO' + 'DY>\n</HT' + 'ML>';
    		var printWin = window.open("","processPrint");
    		printWin.document.open();
    		printWin.document.write(html);
   			printWin.document.close();

    		if (gAutoPrint) printWin.print();
    	} else alert("Browser not supported.");
    }
	</script>
</head>
<body>
<% if (Bean.isLastCheckCard(cd_card1)) {
	
	wpClubCardObject card = new wpClubCardObject(cd_card1);
	%>
	<div id="printableArea"> 
	<table class="table_cheque"><tbody>
		<tr><td align="center" colspan="2"><%=Bean.getTerminalTitle() %></td></tr>
		<tr><td align="center" colspan="2"><%=Bean.getLoginUserServicePlaceName() %></td></tr>
		<tr><td align="center" colspan="2"><%=Bean.getLoginUserServicePlaceAdr() %></td></tr>
		<tr><td align="center" colspan="2">&nbsp;</td></tr>
		<tr><td align="center" colspan="2"><%=Bean.webposXML.getfieldTransl("title_report_card_rests", false) %></td></tr>
		<tr><td align="center" colspan="2">&nbsp;</td></tr>
		<tr><td><%=Bean.webposXML.getfieldTransl("cheque_organization_terminal", false) %></td><td><%=Bean.getCurrentTerm() %></td></tr>
		<tr><td align="center" colspan="2">&nbsp;</td></tr>
		<%=card.getWEBClientCardParamHTML(Bean.loginTerm) %>
		<tr><td align="center" colspan="2">&nbsp;</td></tr>
		<tr><td align="center" colspan="2"><%=Bean.getLoginUserNatPrsFIOInitial() %></td></tr>
		<tr><td align="center" colspan="2"><%=Bean.getLoginUserHideCardNumber() %></td></tr>
	</tbody></table>
	</div>
	<script language="javascript">
	<% if ("Y".equalsIgnoreCase(print)) {%>
	printDivShort('printableArea');
	<% }%>
	</script>
	<%
} else { %>
		<br><span id="error_title"><%=Bean.webposXML.getfieldTransl("operation_error", false) %></span><br>
		<span id="error_description"></span><br><br>
<% } %>
</body>